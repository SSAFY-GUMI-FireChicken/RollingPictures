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

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "section_id")
    private Long id;

    private String startUsername;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "section")
    private List<Round> rounds = new ArrayList<>();

    public Section(long id, String startUsername) {
        this.id = id;
        this.startUsername = startUsername;
    }

    public void changeChannel(Channel channel) {
        this.channel = channel;
        this.channel.getSections().add(this);
    }

    public void addRound(Round round) {
        rounds.add(round);
        round.changeSection(this);
    }
}
