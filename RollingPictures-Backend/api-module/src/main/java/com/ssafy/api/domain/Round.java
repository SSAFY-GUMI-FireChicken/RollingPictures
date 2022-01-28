package com.ssafy.api.domain;

import com.ssafy.core.code.YNCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

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

    private String keyword;

    private String imgSrc;

    @Enumerated(EnumType.STRING)
    private YNCode isKeyword;

    private Integer roundNumber; // 현재 라운드

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeSection(Section section) {
        this.section = section;
        section.addRound(this);
    }
}
