package com.ssafy.api.domain;

import lombok.*;

import javax.persistence.*;

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

    public Section(long id, String startUsername) {
        this.id = id;
        this.startUsername = startUsername;
    }

    public void changeChannel(Channel channel) {
        this.channel = channel;
        this.channel.getSections().add(this);
    }
}
