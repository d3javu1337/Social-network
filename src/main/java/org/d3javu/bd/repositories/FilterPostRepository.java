package org.d3javu.bd.repositories;

import org.d3javu.bd.filter.EPredicateBuildMethod;
import org.d3javu.bd.filter.post.PostFilter;

import java.util.List;

public interface FilterPostRepository {

    List<Long> findAllIdsByTagsFilter(PostFilter filter, EPredicateBuildMethod method);

}
