package com.nofront.plantinggrassbe.domain;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@DynamicInsert
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
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

    @Builder.Default
    @Column(nullable = true, updatable = false, columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    @Builder.Default
    @Column(nullable = true, updatable = false, columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    LocalDateTime updatedDate = LocalDateTime.now();

    private String data;

    private String dataKey;


}
