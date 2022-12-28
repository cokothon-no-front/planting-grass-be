package com.nofront.plantinggrassbe.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor

@DynamicInsert
@Entity
@Data
public class UserSaveData {
    @Column(nullable = false)
    String data;

    @Column(nullable = false)
    String dataKey;

    boolean isPrivate;
}
