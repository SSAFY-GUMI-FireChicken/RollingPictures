package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.ssafy.api.domain.QGameChannelUserInfo.gameChannelUserInfo;

@RequiredArgsConstructor
@Repository
@Transactional
public class GameChannelUserInfoRepositoryImpl implements GameChannelUserInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Optional<Integer> findOrderNum(Long gameChannelId, Long userId) {
        return Optional.ofNullable(queryFactory
                .select(gameChannelUserInfo.orderNum)
                .from(gameChannelUserInfo)
                .where(gameChannelEq(gameChannelId), userEq(userId))
                .fetchOne());
    }

    private BooleanExpression gameChannelEq(Long gameChannelId) {
        return gameChannelId != null ? gameChannelUserInfo.gameChannel.id.eq(gameChannelId) : null;
    }

    private BooleanExpression userEq(Long userId) {
        return userId != null ? gameChannelUserInfo.user.id.eq(userId) : null;
    }
}
