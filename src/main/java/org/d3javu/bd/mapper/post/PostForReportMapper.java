package org.d3javu.bd.mapper.post;

import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.post.PostForReport;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class PostForReportMapper implements Mapper<Post, PostForReport> {
    @Override
    public PostForReport map(Post object) {
        return new PostForReport(
                object.getId(),
                object.getTitle(),
                object.getBody(),
                object.getLikes().size(),
                object.getCreatedAt().format(DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm")),
                object.getAuthor().getFirstName(),
                object.getAuthor().getLastName(),
                "http://localhost:8080/posts/" + object.getId()
        );
    }
}
