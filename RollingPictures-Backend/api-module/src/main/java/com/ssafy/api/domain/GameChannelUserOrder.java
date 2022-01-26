package com.ssafy.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_channel_user_order")
public class GameChannelUserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "game_channel_user_order_id", nullable = false)
    private Long id;

    private Integer orderNum;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_channel_game_channel_id")
    private GameChannel gameChannel;

    public void changeGameChannel(GameChannel gameChannel) {
        this.gameChannel = gameChannel;
        gameChannel.getGameChannelUserOrders().add(this);
    }
}