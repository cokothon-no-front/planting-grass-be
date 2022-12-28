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
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated
    private RoleType roleType;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "gender")
    private String gender;

    @Column(name = "height")
    private Float height;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "body_fat_per")
    private Float bodyFatPer;

    @Column(name = "muscle_per")
    private Float musclePer;

    @Column(name = "is_newbie")
    private boolean isNewbie;

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

