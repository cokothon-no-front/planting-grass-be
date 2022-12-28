package com.nofront.plantinggrassbe.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Column(name = "name")
    private String name;

//    @OneToMany(mappedBy = "subRoutine")
//    List<WorkSet> workSets = new ArrayList<>();
}

