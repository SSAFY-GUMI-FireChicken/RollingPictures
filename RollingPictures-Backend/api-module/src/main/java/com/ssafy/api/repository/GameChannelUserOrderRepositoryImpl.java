package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.GameChannelUserOrder;
import com.ssafy.api.domain.QGameChannelUserOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.ssafy.api.domain.QGameChannelUserOrder.*;

@RequiredArgsConstructor
@Repository
@Transactional
public class GameChannelUserOrderRepositoryImpl implements GameChannelUserOrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Optional<Integer> findOrderNum(Long gameChannelId, Long userId) {
        return Optional.ofNullable(queryFactory
                .select(gameChannelUserOrder.orderNum)
                .from(gameChannelUserOrder)
                .where(gameChannelEq(gameChannelId), userEq(userId))
                .fetchOne());
    }

    private BooleanExpression gameChannelEq(Long gameChannelId) {
        return gameChannelId != null ? gameChannelUserOrder.gameChannel.id.eq(gameChannelId) : null;
    }

    private BooleanExpression userEq(Long userId) {
        return userId != null ? gameChannelUserOrder.user.id.eq(userId) : null;
    }
}
