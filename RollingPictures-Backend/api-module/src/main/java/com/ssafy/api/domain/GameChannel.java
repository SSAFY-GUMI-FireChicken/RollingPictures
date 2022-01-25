package com.ssafy.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.collection.internal.PersistentBag;

import javax.persistence.*;

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

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "gameChannel")
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "gameChannel")
    private List<GameChannelUserOrder> gameChannelUserOrders = new ArrayList<>();

    public void addSection(Section section) {
        section.changeGameChannel(this);
    }

    public void addGameChannelUserOrder(GameChannelUserOrder order) {
        order.changeGameChannel(this);
    }
}