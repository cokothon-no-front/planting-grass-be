package com.nofront.plantinggrassbe.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiCountRequestDto {
    private String workOut;
    private double[][] data = new double[10][4];
    private long startTime;
}
