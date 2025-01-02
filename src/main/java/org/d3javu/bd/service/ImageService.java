package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.d3javu.bd.repositories.ImagesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageService {

    private final ImagesRepository imagesRepository;

    @Value("${app.image.bucket.source}")
    private String bucket;

    @Value("${app.image.bucket.local}")
    private boolean isLocal;

    @Value("${app.image.bucket.port}")
    private String port;

    private final String forPost = bucket+"postimages";
//    private final String  forAvatar = bucket+"\\profileimages";

    @Transactional
    public void uploadAvatar(String path, InputStream content) throws IOException {
        if (isLocal) {
            Path p = Path.of(bucket, "profileimages", path);

            try(content){
                Files.createDirectories(p.getParent());
                Files.write(p, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }else {
            final String url = "http://"+this.bucket+":"+this.port+"/profileimages/"+path;
            new RestTemplate().put(url, content.readAllBytes());
        }

    }

    @SneakyThrows
    public Optional<byte[]> getAvatar(String path) {
        if (isLocal) {
            Path p = Path.of(bucket, "profileimages", path);

            return Files.exists(p) ? Optional.of(Files.readAllBytes(p)) : Optional.empty();
        }
        final String url = "http://"+this.bucket+":"+this.port+"/profileimages/"+path;
        return Optional.ofNullable(new RestTemplate().getForObject(url, byte[].class));
    }

    @Transactional
    public void uploadImage(String path, InputStream content) throws IOException {
        if (isLocal) {
            Path p = Path.of(bucket, "postimages", path);
//        System.out.println("--------------------------------"+true);
            try(content){
                Files.createDirectories(p.getParent());
                Files.write(p, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }else {
            final String url = "http://"+this.bucket+":"+this.port+"/postimages/"+path;
            new RestTemplate().put(url, content.readAllBytes());
        }

    }

    @SneakyThrows
    public Optional<byte[]> getImage(String path) {
        if (isLocal) {
            Path p = Path.of(bucket, "postimages", path);

            return Files.exists(p) ? Optional.of(Files.readAllBytes(p)) : Optional.empty();
        }
        final String url = "http://"+this.bucket+":"+this.port+"/postimages/"+path;
        return Optional.ofNullable(new RestTemplate().getForObject(url, byte[].class));
    }

    @Transactional
    public void deleteImage(Long postId, String path) {
        this.imagesRepository.deleteImagesByPostIdAndPath(postId, path);
    }

}
