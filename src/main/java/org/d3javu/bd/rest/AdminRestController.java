package org.d3javu.bd.rest;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.d3javu.bd.controllers.postController.PostController;
import org.d3javu.bd.mapper.post.PostForReportMapper;
import org.d3javu.bd.models.post.PostForReport;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final PostService postService;
    private final ThymeleafViewResolver thymeleafViewResolver;
    private final LocaleResolver localeResolver;
    private final PostController postController;
    private final PostForReportMapper postForReportMapper;
    private final UserService userService;
    private final TemplateEngine templateEngine;

    @Value("${app.report.generator.port}")
    private int port;

    @Value("${app.report.generator.host}")
    private String host;

    @Value("${app.report.generator.route}")
    private String route;

//    @GetMapping("/report/json")
//    public ResponseEntity<Map<String, List<PostReadDto>>> getTopN(@RequestParam int n) {
//        var map = new HashMap<String, List<PostReadDto>>();
//        map.put("array", this.postService.findTopN(n));
//        map.put("length", n);
//        this.reportService.exportReport(n);
//        return new ResponseEntity<>(map, HttpStatus.OK);
//    }

    @SneakyThrows
    @CrossOrigin(maxAge = 3600, origins = "http://localhost:5488")
    @GetMapping(value = "/report")
    public ResponseEntity<byte[]> getTopN(@RequestParam Integer n) {

        final String url = "http://" + this.host + ":" + this.port + this.route;

        var posts = this.postService.findAllByIds(
                this.postService.findTopN(n)
                        .stream()
                        .map(PostForReport::id)
                        .collect(Collectors.toSet())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var filename = Hashing.sha256().hashLong(posts.stream().map(PostForReport::hashCode).mapToLong(Long::valueOf).sum()).toString() + ".pdf";

        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setAccept(Collections.singletonList(MediaType.ALL));

        var vals = new HashMap<String, Object>();
        vals.put("posts", posts);

//        final IContext context = new Context(null, vals);
//        var rendered = this.templateEngine.process("post/report", context);
//        var temp = new RestTemplate().postForObject(url, Map.of("html", rendered), String.class);

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        List<MediaType> supportedApplicationTypes = new ArrayList<>();
        MediaType pdfApplication = new MediaType("application","pdf");
        supportedApplicationTypes.add(pdfApplication);

        byteArrayHttpMessageConverter.setSupportedMediaTypes(supportedApplicationTypes);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(byteArrayHttpMessageConverter);
        messageConverters.add(new GsonHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        var restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(messageConverters);

        var result = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(vals, headers), byte[].class);

//        var temp = new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
//        var rt = new RestTemplate();
//        System.out.println(temp.length);

        var res = result.getBody();
        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }

    public User getCurrentUser(){
        var userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(userEmail);
    }
}
