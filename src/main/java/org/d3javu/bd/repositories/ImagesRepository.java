package org.d3javu.bd.repositories;

import org.d3javu.bd.models.images.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {


    @Modifying
    @Query(value = "delete from images where post_id= :postId and path= :imagesPath",
    nativeQuery = true)
    void deleteImagesByPostIdAndPath(Long postId, String imagesPath);

    @Query(value = "select i.path from images i where i.post_id= :postId",
            nativeQuery = true)
    List<String> findAllPathsByPostId(Long postId);

}
