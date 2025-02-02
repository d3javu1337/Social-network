package org.d3javu.bd.dto.post;

import lombok.Data;
import lombok.ToString;
import lombok.Value;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class PostCreateDto {

    public String title;
    public String body;
    public Set<Tag> tags;
    public User author;
    public List<MultipartFile> images;


}
