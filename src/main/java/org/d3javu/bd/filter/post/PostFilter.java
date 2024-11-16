package org.d3javu.bd.filter.post;

import lombok.Data;
import org.d3javu.bd.models.tag.Tag;

import java.util.Set;

public record PostFilter(Set<Tag> tags) {
}
