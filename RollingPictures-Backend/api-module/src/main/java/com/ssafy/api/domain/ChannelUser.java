package com.ssafy.api.domain;

import com.ssafy.core.code.GamePlayState;
import com.ssafy.core.code.YNCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "channel_user")
public class ChannelUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "channel_user_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private YNCode isLeader;

    @Enumerated(EnumType.STRING)
    private GamePlayState gamePlayState;

    @Enumerated(EnumType.STRING)
    private YNCode isMute;
}