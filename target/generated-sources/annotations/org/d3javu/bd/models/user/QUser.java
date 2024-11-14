package org.d3javu.bd.models.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 945628855L;

    public static final QUser user = new QUser("user");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath customLink = createString("customLink");

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    public final SetPath<User, QUser> followers = this.<User, QUser>createSet("followers", User.class, QUser.class, PathInits.DIRECT2);

    public final SetPath<User, QUser> follows = this.<User, QUser>createSet("follows", User.class, QUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lastName = createString("lastName");

    public final StringPath password = createString("password");

    public final SetPath<org.d3javu.bd.models.tag.Tag, org.d3javu.bd.models.tag.QTag> preferredTags = this.<org.d3javu.bd.models.tag.Tag, org.d3javu.bd.models.tag.QTag>createSet("preferredTags", org.d3javu.bd.models.tag.Tag.class, org.d3javu.bd.models.tag.QTag.class, PathInits.DIRECT2);

    public final EnumPath<Roles> role = createEnum("role", Roles.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

