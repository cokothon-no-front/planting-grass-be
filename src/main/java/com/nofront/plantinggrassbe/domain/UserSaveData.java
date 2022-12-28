package com.nofront.plantinggrassbe.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveData {

    String data;

    String dataKey;

    boolean isPrivate;
}
