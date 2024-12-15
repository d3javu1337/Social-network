package org.d3javu.bd.rest;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.service.PostService;
import org.d3javu.bd.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestController {

    private final PostService postService;
    private final ReportService reportService;

    @GetMapping
    public List<PostReadDto> getTopN(@RequestParam int n) {
        this.reportService.exportReport(n);
        return this.postService.findTopN(n);
    }

//    @GetMapping
//    public ResponseEntity<byte[]> getPdf(@RequestParam String path) {
//        return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @GetMapping("/test")
//    public ResponseEntity<Object> test() {
//        return new ResponseEntity<>(new Object(), HttpStatus.OK);
//    }

}
