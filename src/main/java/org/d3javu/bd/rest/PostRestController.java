package org.d3javu.bd.rest;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostRestController {

    private final PostService postService;

    @GetMapping(value = "/{id}/images", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] findImages(@PathVariable Long id) {
        var imgs = this.postService.findImages(id);
        if (!imgs.isEmpty()){
            return imgs.get(0);
        }else{
            return new byte[0];
        }
    }

}
