package org.d3javu.bd.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.QPredicates;
import org.d3javu.bd.filter.EPredicateBuildMethod;
import org.d3javu.bd.filter.post.PostFilter;
import org.d3javu.bd.mapper.post.PostReadMapper;
import org.d3javu.bd.models.post.Post;

import java.util.List;

import static org.d3javu.bd.models.post.QPost.post;


@RequiredArgsConstructor
public class FilterPostRepositoryImpl implements FilterPostRepository {

    private final EntityManager em;
    private final PostReadMapper postReadMapper;

    @Override
    public List<Post> findAllByTagsFilter(PostFilter filter, EPredicateBuildMethod predicateBuildMethod) {
        var QPredicate = QPredicates.builder();
        for(var x : filter.tags()){
            QPredicate.add(x, post.tags::contains);
        }
        var predicate = switch (predicateBuildMethod){
            case AND -> QPredicate.buildAnd();
            case OR -> QPredicate.buildOr();
            default -> throw new IllegalStateException("Unexpected value: " + predicateBuildMethod);
        };
//        var predicate = QPredicate.buildOr();

        return new JPAQuery<Post>(em).select(post).from(post)
                .where(predicate).fetch();
    }
}
