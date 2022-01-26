package com.ssafy.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.GameChannelUserOrder;
import com.ssafy.api.domain.QGameChannelUserOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.ssafy.api.domain.QGameChannelUserOrder.*;

@RequiredArgsConstructor
@Repository
@Transactional
public class GameChannelUserOrderRepositoryImpl implements GameChannelUserOrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public GameChannelUserOrder findByUserid(Long userId) {
        return queryFactory
                .select(gameChannelUserOrder)
                .from(gameChannelUserOrder)
                .where(gameChannelUserOrder.user.id.eq(userId))
                .fetchOne();
    }
}
