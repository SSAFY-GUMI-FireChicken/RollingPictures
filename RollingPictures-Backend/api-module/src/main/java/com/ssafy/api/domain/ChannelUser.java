package com.ssafy.api.domain;

import com.ssafy.core.code.GamePlayState;
import com.ssafy.core.code.YNCode;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "channel_user",
    uniqueConstraints = {
            @UniqueConstraint(
                    columnNames={"user_id"}
            )
    }
)
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

    public void changeChannel(Channel channel) {
        this.channel = channel;
    }

    public void changeMute(YNCode code) {
        isMute = code;
    }

    public void changeIsLeader(YNCode code) {
        isLeader = code;
    }
}