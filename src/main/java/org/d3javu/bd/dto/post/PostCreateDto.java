package org.d3javu.bd.dto.post;

import lombok.Value;
import org.d3javu.bd.models.tag.Tag;

import java.util.HashSet;
import java.util.Set;

@Value
public class PostCreateDto {

    public String title;
    public String content;
    public Set<Tag> tags = new HashSet<>();

}
