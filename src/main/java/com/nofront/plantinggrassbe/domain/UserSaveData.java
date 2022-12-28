package com.nofront.plantinggrassbe.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor

public class UserSaveData {
    @Id
    @Column(nullable = false)
    String dataKey;

    @Column(nullable = false)
    String data;

    @Column
    boolean isPrivate;
}
