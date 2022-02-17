package com.ssafy.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_channel")
public class GameChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_channel_id", nullable = false)
    private Long id;

    private String code;

    private Integer curRoundNumber;

    private Integer conPeopleCnt;

    private Integer donePeopleCnt;

    private LocalDateTime startDate;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "gameChannel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "gameChannel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameChannelUserInfo> gameChannelUserInfos = new ArrayList<>();

    public void addSection(Section section) {
        section.changeGameChannel(this);
    }

    public void addGameChannelUserInfo(GameChannelUserInfo info) {
        info.changeGameChannel(this);
    }

    public void changeConPeopleCnt(int amount) {
        conPeopleCnt += amount;
    }

    public void addCurRoundNumber() {
        curRoundNumber++;
    }

    public void changeDonePeopleCnt(int donePeopleCnt) {
        this.donePeopleCnt = donePeopleCnt;
    }

    public void changeStartDate() {
        startDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    @Override
    public String toString() {
        return "GameChannel{" +
                "id=" + id +
                ", code='" + code + '\'' +
                '}';
    }
}