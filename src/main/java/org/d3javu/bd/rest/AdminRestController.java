package org.d3javu.bd.rest;

import com.google.gson.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.d3javu.bd.controllers.postController.PostController;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.dto.user.CompactUserReadDto;
import org.d3javu.bd.mapper.post.PostForReportMapper;
import org.d3javu.bd.models.post.PostForReport;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.ReportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.spring6.expression.ThymeleafEvaluationContext;
import org.thymeleaf.spring6.view.ThymeleafView;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.spring6.view.reactive.ThymeleafReactiveView;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final PostService postService;
    private final ReportService reportService;
    private final ThymeleafViewResolver thymeleafViewResolver;
    private final LocaleResolver localeResolver;
    private final PostController postController;
    private final PostForReportMapper postForReportMapper;

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

    @CrossOrigin(maxAge = 3600, origins = "http://localhost:5488")
    @GetMapping("/report")
    public ResponseEntity<String> getTopN(@RequestParam Integer n) {
//        var map = new HashMap<String, List<?>>();
//        map.put("array", this.postService.findTopN(n));
//        map.put("length", List.of(n));

        final String url = "http://" + this.host + ":" + this.port + this.route;

//        var t = new HashMap<String, Object>();
//        t.put("id", 0);
//        t.put("firstName", "Ivan");
//        t.put("lastName", "Ivanov");
//        t.put("avatarPath", "netu");
//
//


//
//        var topPosts = this.postService.findTopN(n)
//                .stream()
//                .map(en -> this.postForReportMapper.map(en))
//                .collect(Collectors.toList());
//        var temp = new RestTemplate().postForObject(url, topPosts, String.class);
        var emm = new ExtendedModelMap();
        var html = this.postController.findAllByIds(emm, this.postService.findTopN(n)
                .stream()
                .map(PostForReport::id)
                .collect(Collectors.toSet())
        );
        var map = new HashMap<String, ExtendedModelMap>();

        System.out.println(html);


        var temp = new RestTemplate().postForObject(url, html, String.class);
        System.out.println(temp);
//        System.out.printf("%s %s%n", n, topPosts.size());

//        this.reportService.exportReport(n);
        return new ResponseEntity<>(temp, HttpStatus.OK);
    }


//    @SneakyThrows
//    @GetMapping(value = "/report/html", produces = MediaType.TEXT_HTML_VALUE)
//    public String getTopNHtml(@RequestParam int n, Model model, HttpServletRequest request, HttpServletResponse response) {
//        model.addAttribute("posts", this.postService.findTopN(n));
//        model.addAttribute("currentUser", new CompactUserReadDto(31L, "", "", ""));
//////        var t = new UrlTemplateResolver();
//////        t.set
//////        return UrlTemplateResolver model;
////
//        var modelAndView = new ModelAndView("post/posts");
//        modelAndView.addAllObjects(model.asMap());
//
//        View resolved = thymeleafViewResolver.resolveViewName(modelAndView.getViewName(), localeResolver.resolveLocale(request));
//        resolved.render(model.asMap(), request, response);
//        return modelAndView.toString();
//    }

//    @GetMapping
//    public ResponseEntity<byte[]> getPdf(@RequestParam String path) {
//        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @GetMapping("/test")
//    public ResponseEntity<Object> test() {
//        return new ResponseEntity<>(new Object(), HttpStatus.OK);
//    }

}
