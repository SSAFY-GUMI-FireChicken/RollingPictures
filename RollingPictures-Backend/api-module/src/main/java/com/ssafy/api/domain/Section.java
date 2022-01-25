package com.ssafy.api.domain;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Section extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "section_id")
    private Long id;

    private String code;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "game_channel_id")
    private GameChannel gameChannel;

    @OneToMany(mappedBy = "section")
    private List<Round> rounds = new ArrayList<>();

    public void addRound(Round round) {
        rounds.add(round);
        round.changeSection(this);
    }
}
