package com.ssafy.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "round_id")
    private Long id;

    private String work;

    private int roundNumber; // 현재 라운드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    public void changeSection(Section section) {
        this.section = section;
        section.addRound(this);
    }
}
