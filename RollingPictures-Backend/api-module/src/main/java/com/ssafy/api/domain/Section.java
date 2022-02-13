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

    private Integer startOrder;

    @OneToOne
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "game_channel_id")
    private GameChannel gameChannel;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Round> rounds = new ArrayList<>();

    public void changeGameChannel(GameChannel gameChannel) {
        this.gameChannel = gameChannel;
        gameChannel.getSections().add(this);
    }

    public void addRound(Round round) {
        round.changeSection(this);
    }
}
