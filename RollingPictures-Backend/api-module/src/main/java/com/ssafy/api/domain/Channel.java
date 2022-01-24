package com.ssafy.api.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "channel_id")
    private Long id;

    @Column(length = 2)
    private int maxPeopleCnt;

    @Column(length = 2)
    private int curPeopleCnt;

    @Column(length = 2)
    private int roundCount;

    @Column
    private boolean isPlaying;

    @Column(length = 3)
    private LocalDateTime roundStartTime;

    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    @Column(name = "code", unique = true, length = 120)
    private String code;

    @OneToMany(mappedBy = "channel", cascade = {CascadeType.REMOVE})
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    List<User> users = new ArrayList<>();

    public boolean addUser(User user) {
        if(users == null)
            users = new ArrayList<>();

        return this.users.add(user);
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.changeChannel(this);
    }
}
