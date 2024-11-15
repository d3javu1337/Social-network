package org.d3javu.bd.mapper.tag;

import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.tag.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagToDtoMapper implements Mapper<Tag, TagDto> {
    @Override
    public TagDto map(Tag object) {
        var dto = new TagDto(
                object.getId(),
                object.getBody(),
                object.getDescription()
        );
        return dto;
    }

//    @Override
//    public TagDto map(Tag from, TagDto to) {
//        to.body= from.getBody();;
//    }
}
