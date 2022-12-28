package com.nofront.plantinggrassbe.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaticURI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "uri")
    private String uri;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
