package com.ssafy.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static javax.persistence.FetchType.LAZY;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "progress_id")
    private Long id;

    private LocalDateTime startDate;

    private Integer donePeopleCnt;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "game_channel_id")
    private GameChannel gameChannel;

    public void changeDonePeopleCnt(int donePeopleCnt) {
        this.donePeopleCnt = donePeopleCnt;
    }

    public void changeStartDate() {
        startDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }
}
