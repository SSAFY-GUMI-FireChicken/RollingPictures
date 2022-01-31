package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.domain.QChannelUser;
import com.ssafy.core.code.YNCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import static com.ssafy.api.domain.QChannelUser.*;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class ChannelUserRepositoryImpl implements ChannelUserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public ChannelUser findLeaderOfChannel(Long channelId) {
        return queryFactory
                .selectFrom(channelUser)
                .where(
                        channelEq(channelId),
                        isLeader()
                )
                .fetchOne();
    }

    private BooleanExpression channelEq(Long channelId) {
        return channelId != null ? channelUser.channel.id.eq(channelId) : null;
    }

    private BooleanExpression isLeader() {
        return channelUser.isLeader.eq(YNCode.Y);
    }
}
