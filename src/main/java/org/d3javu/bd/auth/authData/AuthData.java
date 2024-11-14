//package org.d3javu.bd.auth.authData;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.d3javu.bd.models.user.Roles;
//import org.d3javu.bd.models.user.User;
//
//@Data
//@Entity
//@NoArgsConstructor
//public class AuthData {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String login;
//
//    private String passwordHash;
//
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private User user;
//
//    @Enumerated(EnumType.STRING)
//    Roles role;
//
//    public AuthData(String login, String passwordHash, User user) {
//        this.login = login;
////        this.passwordHash = "{noop}"+passwordHash;
//        this.passwordHash = passwordHash;
//        this.user = user;
//        this.role = Roles.user;
//        user.setAuthData(this);
//    }
//}
