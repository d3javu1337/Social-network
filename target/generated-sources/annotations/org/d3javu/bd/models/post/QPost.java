package org.d3javu.bd.models.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1686051881L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final org.d3javu.bd.models.user.QUser author;

    public final StringPath body = createString("body");

    public final SetPath<org.d3javu.bd.models.comment.Comment, org.d3javu.bd.models.comment.QComment> comments = this.<org.d3javu.bd.models.comment.Comment, org.d3javu.bd.models.comment.QComment>createSet("comments", org.d3javu.bd.models.comment.Comment.class, org.d3javu.bd.models.comment.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<org.d3javu.bd.models.user.User, org.d3javu.bd.models.user.QUser> likes = this.<org.d3javu.bd.models.user.User, org.d3javu.bd.models.user.QUser>createSet("likes", org.d3javu.bd.models.user.User.class, org.d3javu.bd.models.user.QUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> likesCount = createNumber("likesCount", Long.class);

    public final SetPath<org.d3javu.bd.models.tag.Tag, org.d3javu.bd.models.tag.QTag> tags = this.<org.d3javu.bd.models.tag.Tag, org.d3javu.bd.models.tag.QTag>createSet("tags", org.d3javu.bd.models.tag.Tag.class, org.d3javu.bd.models.tag.QTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final SetPath<org.d3javu.bd.models.user.User, org.d3javu.bd.models.user.QUser> views = this.<org.d3javu.bd.models.user.User, org.d3javu.bd.models.user.QUser>createSet("views", org.d3javu.bd.models.user.User.class, org.d3javu.bd.models.user.QUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> viewsCount = createNumber("viewsCount", Long.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new org.d3javu.bd.models.user.QUser(forProperty("author")) : null;
    }

}

