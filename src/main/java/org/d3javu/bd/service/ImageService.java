package org.d3javu.bd.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ImageService {

    @Value("${app.image.bucket}")
    private String bucket;

    private final String forPost = bucket+"postimages";
//    private final String  forAvatar = bucket+"\\profileimages";

    @Transactional
    public void uploadAvatar(String path, InputStream content) throws IOException {
        Path p = Path.of(bucket, "profileimages", path);

        try(content){
            Files.createDirectories(p.getParent());
            Files.write(p, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }

    }

    @SneakyThrows
    public Optional<byte[]> getAvatar(String path) {
        Path p = Path.of(bucket, "profileimages", path);

        return Files.exists(p) ? Optional.of(Files.readAllBytes(p)) : Optional.empty();
    }

    @Transactional
    public void uploadImage(String path, InputStream content) throws IOException {
        Path p = Path.of(bucket, "postimages", path);
//        System.out.println("--------------------------------"+true);
        try(content){
            Files.createDirectories(p.getParent());
            Files.write(p, content.readAllBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }

    }

    @SneakyThrows
    public Optional<byte[]> getImage(String path) {
        Path p = Path.of(bucket, "postimages", path);

        return Files.exists(p) ? Optional.of(Files.readAllBytes(p)) : Optional.empty();
    }

}
