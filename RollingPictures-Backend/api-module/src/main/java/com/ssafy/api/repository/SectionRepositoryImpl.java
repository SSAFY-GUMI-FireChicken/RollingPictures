package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.Round;
import com.ssafy.api.domain.Section;
import com.ssafy.api.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.ssafy.api.domain.QRound.round;
import static com.ssafy.api.domain.QSection.*;

@RequiredArgsConstructor
@Repository
@Transactional
public class SectionRepositoryImpl implements SectionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


    @Override
    public List<Section> findSectionByGameChannelId(Long gameChannelId) {
        return queryFactory
                .selectFrom(section)
                .where(gameChannelEq(gameChannelId))
                .fetch();
    }

    @Override
    public Optional<Section> findSection(Long gameChannelId, Integer startOrder) {
        return Optional.ofNullable(queryFactory
                .selectFrom(section)
                .where(gameChannelEq(gameChannelId), startOrderEq(startOrder))
                .fetchOne());
    }

    @Override
    public Optional<Section> findSection(Long gameChannelId, Long userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(section)
                .where(gameChannelEq(gameChannelId), userIdEq(userId))
                .fetchOne());
    }

    @Override
    public List<User> findOrder(Long gameChannelId) {
        return queryFactory
                .select(section.user)
                .from(section)
                .where(gameChannelEq(gameChannelId))
                .fetch();
    }

    @Override
    public List<Round> findRounds(Long sectionId) {
        return queryFactory
                .selectFrom(round)
                .where(sectionIdEq(sectionId))
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? section.user.id.eq(userId) : null;
    }

    private BooleanExpression sectionIdEq(Long sectionId) {
        return sectionId != null ? section.id.eq(sectionId) : null;
    }

    private BooleanExpression gameChannelEq(Long gameChannelId) {
        return gameChannelId != null ? section.gameChannel.id.eq(gameChannelId) : null;
    }

    private BooleanExpression startOrderEq(Integer startOrder) {
        return startOrder != null ? section.startOrder.eq(startOrder) : null;
    }
}
