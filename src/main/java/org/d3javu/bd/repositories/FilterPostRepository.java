package org.d3javu.bd.repositories;

import org.d3javu.bd.dto.post.PostReadDto;
import org.d3javu.bd.filter.EPredicateBuildMethod;
import org.d3javu.bd.filter.post.PostFilter;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;

import java.util.List;
import java.util.Set;

public interface FilterPostRepository {

    List<Post> findAllByTagsFilter(PostFilter filter, EPredicateBuildMethod method);

//    List<PostReadDto> findAllDto();


}
