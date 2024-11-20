package org.d3javu.bd.rest;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.models.images.Images;
import org.d3javu.bd.service.ImageService;
import org.d3javu.bd.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@RestController
public class PostRestController {

    private final PostService postService;
    private final ImageService imageService;

    @GetMapping(value = "/{id}/images/{imagePath}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] findImages(@PathVariable Long id, @PathVariable("imagePath") String imagePath) {
        System.out.println(imagePath);
        var img = this.imageService.getImage(imagePath);
        return img.get();
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs.get(0);
//        }else{
//            return new byte[0];
//        }
    }

//    @GetMapping(value = "/{id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<String> findImages(@PathVariable Long id) {
//        return this.postService.findPostById(id)
//                .map(en -> {
//                    return en.getImages()
//                            .stream().map(Images::getPath)
//                            .collect(Collectors.toList());
//                })
//                .orElse(new ArrayList<String>());
//    }

//    @GetMapping(value = "/{id}/images", produces = MediaType.APPLICATION_JSON_VALUE)
//    public  ResponseEntity<IMGS> findImages(@PathVariable Long id) {
//        var imgs = new IMGS(this.postService.findPostById(id)
//                .map(en -> {
//                    return en.getImages()
//                            .stream().map(Images::getPath)
//                            .collect(Collectors.toList());
//                })
//                .orElse(new ArrayList<String>()));
//        return new ResponseEntity<>(imgs, HttpStatus.OK);
//    }
//
//    record IMGS(List<String> paths){}

//    @GetMapping(value = "/{id}/images", produces = MediaType.IMAGE_PNG_VALUE)
//    public byte[] findImages(@PathVariable Long id) {
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs.get(0);
//        }else{
//            return new byte[0];
//        }
//    }

//    @RequestMapping(value = "/{id}/images", produces =  MediaType.,
//            method = RequestMethod.GET)
//    public List<byte[]> findImages(@PathVariable Long id) {
//        var imgs = this.postService.findImages(id);
//        if (!imgs.isEmpty()){
//            return imgs;
//        }else{
//            System.out.println("shit happens");
//            return List.of(new byte[0]);
//        }
//    }

}
