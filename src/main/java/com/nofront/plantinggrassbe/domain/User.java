package com.nofront.plantinggrassbe.domain;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
// 이거 바꾸면 UserDetailsServiceImpl 빌더 바꿔 주기
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name="user",
        uniqueConstraints={
                @UniqueConstraint(
                        name= "user_unique",
                        columnNames={"username", "provider"}
                )
        }
)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = true, unique = true)
    private String username;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "provider", nullable = true)
    private String provider;

//    @Enumerated
//    private RoleType roleType;

    @Column(name = "refresh_token")
    private String refreshToken;

//    @OneToMany(mappedBy = "user")
//    List<Routine> routines = new ArrayList<>();
}
/*
id -> auto

provider
username
--------->bytoken

nickname
isnewbie
gender
height
weight
bodyfatper
muscleper




 */

