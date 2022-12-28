package com.nofront.plantinggrassbe.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@DynamicInsert
@Entity
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
public class UserSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @ManyToOne(targetEntity = User.class)
//    @JoinColumn(name = "username")
    private String username;


    @Column()
    boolean isPrivate;

    @CreatedDate
    @Column()
    LocalDateTime createdDate;

    @LastModifiedDate
    @Column()
    LocalDateTime updatedDate;

    private String data;

    private String dataKey;

}
