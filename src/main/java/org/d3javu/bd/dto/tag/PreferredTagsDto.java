package org.d3javu.bd.dto.tag;

import lombok.Value;
import org.d3javu.bd.models.tag.Tag;

import java.util.Set;

@Value
public class PreferredTagsDto {

    Set<Tag> tags;

}
