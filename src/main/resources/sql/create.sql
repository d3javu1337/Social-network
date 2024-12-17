create database bd;

CREATE TABLE comments
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    body        TEXT,
    post_id     BIGINT,
    user_id     BIGINT,
    likes_count BIGINT,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE comments_likes
(
    comment_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT pk_comments_likes PRIMARY KEY (comment_id, user_id)
);

CREATE TABLE follows
(
    followed_id BIGINT NOT NULL,
    follower_id BIGINT NOT NULL,
    CONSTRAINT pk_follows PRIMARY KEY (followed_id, follower_id)
);

CREATE TABLE images
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    post_id BIGINT,
    path    VARCHAR(255),
    CONSTRAINT pk_images PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title       VARCHAR(255),
    body        TEXT,
    author_id   BIGINT,
    views_count BIGINT,
    likes_count BIGINT,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE posts_likes
(
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_posts_likes PRIMARY KEY (post_id, user_id)
);

CREATE TABLE posts_tags
(
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    CONSTRAINT pk_posts_tags PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE posts_views
(
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_posts_views PRIMARY KEY (post_id, user_id)
);

CREATE TABLE tags
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    body        VARCHAR(30)                             NOT NULL,
    description VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id)
);

CREATE TABLE user_preferred_tags
(
    tag_id  BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_preferred_tags PRIMARY KEY (tag_id, user_id)
);

CREATE TABLE users
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    email       VARCHAR(255),
    custom_link VARCHAR(255),
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    password    VARCHAR(255),
    role        VARCHAR(255),
    avatar_path VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE tags
    ADD CONSTRAINT uc_tags_body UNIQUE (body);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE images
    ADD CONSTRAINT FK_IMAGES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE comments_likes
    ADD CONSTRAINT fk_comlik_on_comment FOREIGN KEY (comment_id) REFERENCES comments (id);

ALTER TABLE comments_likes
    ADD CONSTRAINT fk_comlik_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE follows
    ADD CONSTRAINT fk_follows_on_followed FOREIGN KEY (followed_id) REFERENCES users (id);

ALTER TABLE follows
    ADD CONSTRAINT fk_follows_on_follower FOREIGN KEY (follower_id) REFERENCES users (id);

ALTER TABLE posts_likes
    ADD CONSTRAINT fk_poslik_on_post FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE posts_likes
    ADD CONSTRAINT fk_poslik_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE posts_tags
    ADD CONSTRAINT fk_postag_on_post FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE posts_tags
    ADD CONSTRAINT fk_postag_on_tag FOREIGN KEY (tag_id) REFERENCES tags (id);

ALTER TABLE posts_views
    ADD CONSTRAINT fk_posvie_on_post FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE posts_views
    ADD CONSTRAINT fk_posvie_on_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_preferred_tags
    ADD CONSTRAINT fk_usepretag_on_tag FOREIGN KEY (tag_id) REFERENCES tags (id);

ALTER TABLE user_preferred_tags
    ADD CONSTRAINT fk_usepretag_on_user FOREIGN KEY (user_id) REFERENCES users (id);