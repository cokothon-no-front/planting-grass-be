package com.nofront.plantinggrassbe.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sub_routine_id")
    private SubRoutine subRoutine;

    @ManyToOne
    @JoinColumn(name = "work_out_id")
    private WorkOut workOut;
}


