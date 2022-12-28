package com.nofront.plantinggrassbe.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@DynamicInsert
@Entity
@Data
public class UserSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false)
    boolean isPrivate;

    @Column(nullable = false)
    LocalDateTime createdDate;

    private String data;

    private String dataKey;

    public UserSave(){
        createdDate = LocalDateTime.now();
    }

}
