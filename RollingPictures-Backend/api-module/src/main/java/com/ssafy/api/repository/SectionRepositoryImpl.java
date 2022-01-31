package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.QSection;
import com.ssafy.api.domain.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

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
                .where(gameChannelIdEq(gameChannelId))
                .fetch();
    }

    private BooleanExpression gameChannelIdEq(Long gameChannelId) {
        return gameChannelId != null ? section.gameChannel.id.eq(gameChannelId) : null;
    }
}
