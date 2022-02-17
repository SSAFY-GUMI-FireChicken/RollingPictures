package com.ssafy.api.domain;

import com.ssafy.api.dto.res.UserInfoResDTO;
import com.ssafy.core.code.YNCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Builder
@Getter
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

    @Column(length = 30)
    private String title;

    @Column(length = 2)
    private int maxPeopleCnt;

    @Column(length = 2)
    private int curPeopleCnt;

    @Enumerated(EnumType.STRING)
    private YNCode isPlaying;

    @Column(unique = true, length = 120)
    private String code;

    @Enumerated(EnumType.STRING)
    private YNCode isPublic;

    @OneToMany(mappedBy = "channel", fetch = LAZY, cascade = CascadeType.REMOVE)
    private List<ChannelUser> channelUsers = new ArrayList<>();

    @OneToOne(mappedBy = "channel", fetch = LAZY, cascade = CascadeType.REMOVE)
    private GameChannel gameChannel;

    public void addChannelUser(ChannelUser channelUser) {
        if(channelUsers == null ){
            channelUsers = new ArrayList<>();
        }
        channelUsers.add(channelUser);
        channelUser.changeChannel(this);
    }

    public void changeCurPeopleCnt(int amount) {
        curPeopleCnt += amount;
    }

    public void changeIsPlaying(YNCode isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeMaxPeopleCnt(int maxPeopleCnt) {
        this.maxPeopleCnt = maxPeopleCnt;
    }

    public void changeIsPublic(YNCode isPublic) {
        this.isPublic = isPublic;
    }

    public List<UserInfoResDTO> changeToUserInfo() {
        List<UserInfoResDTO> result  = new ArrayList<>();

        channelUsers.forEach(x -> result.add(UserInfoResDTO
                .builder()
                .id(x.getUser().getId())
                .nickname(x.getUser().getNickname())
                .isLeader(x.getIsLeader())
                .build()));

        return result;
    }
}
