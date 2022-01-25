package com.ssafy.api.domain;

import com.ssafy.core.code.YNCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "channel",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames={"code"}
                )
        }
)
public class Channel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "channel_id")
    private Long id;

    @Column(length = 2)
    private int maxPeopleCnt;

    @Column(length = 2)
    private int curPeopleCnt;

    @Enumerated(EnumType.STRING)
    private YNCode isPlaying;

    @Column(unique = true, length = 120)
    private String code;

    @OneToMany(mappedBy = "channel")
    private List<ChannelUser> channelUsers = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    private GameChannel gameChannel;

}
