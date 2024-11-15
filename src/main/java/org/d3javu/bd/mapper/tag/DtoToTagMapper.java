package org.d3javu.bd.mapper.tag;

import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.mapper.Mapper;
import org.d3javu.bd.models.tag.Tag;
import org.springframework.stereotype.Component;

@Component
public class DtoToTagMapper implements Mapper<TagDto, Tag> {
    @Override
    public Tag map(TagDto object) {
        var tag = new Tag();
        tag.setId(object.getId());
        tag.setBody(object.getBody());
        tag.setDescription(object.getDescription());
        return tag;
    }

    @Override
    public Tag map(TagDto from, Tag to) {
        to.setId(from.getId());
        to.setBody(from.getBody());
        to.setDescription(from.getDescription());
        return to;
    }
}
