package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.QGameChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.ssafy.api.domain.QGameChannel.*;
import static com.ssafy.api.domain.QGameChannelUserOrder.*;

@RequiredArgsConstructor
@Repository
@Transactional
public class GameChannelRepositoryImpl implements GameChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public void deleteByChannelId(Long channelId) {
        queryFactory
                .delete(gameChannel)
                .where(channelEq(channelId))
                .execute();
    }

    private BooleanExpression channelEq(Long channelId) {
        return channelId != null ? gameChannel.channel.id.eq(channelId) : null;
    }
}
