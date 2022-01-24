package com.ssafy.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Channel extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "channel_id")
    private Long id;

    private int maxPeopleCnt;
    private int curPeopleCnt;
    private String leader;
    private String code;

    @OneToMany(mappedBy = "channel")
    private List<Section> sections = new ArrayList<>();

    public Channel(long id, int maxPeopleCnt, int curPeopleCnt, String leader, String code) {
        this.id = id;
        this.maxPeopleCnt = maxPeopleCnt;
        this.curPeopleCnt = curPeopleCnt;
        this.leader = leader;
        this.code = code;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.changeChannel(this);
    }
}
