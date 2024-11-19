package org.d3javu.bd.controllers.shit;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.models.user.Roles;
import org.d3javu.bd.models.user.User;
import org.d3javu.bd.repositories.CommentRepository;
import org.d3javu.bd.repositories.PostRepository;
import org.d3javu.bd.repositories.TagRepository;
import org.d3javu.bd.repositories.UserRepository;
import org.d3javu.bd.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@RequiredArgsConstructor
@Controller
public class TestController implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String index(Model model) {


        return "index";
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        var tags = List.of(new Tag("tag1"), new Tag("tag2"), new Tag("tag3"));
        tagRepository.saveAll(tags);
//        tagRepository.flush();
        var tag1 = tagRepository.findByBody("tag1").orElse(null);
        var tag2 = tagRepository.findByBody("tag2").orElse(null);

        var user1 = new User("Ivan", "Ivanov", "vanya@jmail.org", this.passwordEncoder.encode("124"));
        var user2 = new User("Petr", "Petrov", "petya@jmail.org", this.passwordEncoder.encode("petya228"));
        var user4 = new User("Egor", "Trunov", "admin", this.passwordEncoder.encode("admin"));
        user4.setRole(Roles.admin);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user4);

        user1 = userRepository.findAll().get(0);
        user2 = userRepository.findAll().get(1);

        var tags1 = tagRepository.findAll();

        var post1 = new Post("testPost", "abababababababa", Set.of(tag1));
        post1.linkUser(user1);
        postRepository.save(post1);


        var post2 = new Post("testPost2", "xaxaxaxaxaxax",  Set.of((tags1.toArray(new Tag[0]))));
        post2.linkUser(user2);
        postRepository.save(post2);

//        user2.addComment("ololo", post1);
//        commentRepository.save()

        user1.follow(user2);
        user2.follow(user1);

        userRepository.save(user2);
        userRepository.save(user1);

        user1 = userRepository.findAll().get(0);
        user2 = userRepository.findAll().get(1);

        var user3 = userRepository.findFollowersById(user2.getId()).get(0);

        System.out.println(user1.equals(user3));
        System.out.println(user1);
        System.out.println(user3);

        post1.like(user3);
        post1.like(user2);
        post1.like(user1);
        post1.like(user1);

        System.out.println(post1.getLikes());

        post1.unlike(user3);

        System.out.println(post1.getLikes());

        System.out.println(post1.getViews());

//        post1.getComments().get(0).like(user3);

        postRepository.findAllByTagId(1L).forEach(e -> System.out.println(e.getBody()));

        System.out.println(userService.findById(1L));
//        UserReadDto actual = userService.create(new UserEditDto(
//                "Sanya",
//                "Aleksandrovich",
//                "sanya@jmail.org",
//                null,
//                "{noop}123",
//                new HashSet<>()
//        ));
//        System.out.println(actual);
//        System.out.println(userRepository.findAll());
//        commentRepository.saveAll(post.getComments());

    }
}
