package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ssafy.api.domain.QRound.round;
import static com.ssafy.api.domain.QSection.section;

public class RoundRepoImpl implements RoundRepoCommon {
    private final JPAQueryFactory queryFactory;
    private EntityManager em;

    public RoundRepoImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

//    @Override
//    public List<String> findHostKeyword(int sectionId) {
//        return queryFactory
//                .select(round.imgSrc)
//                .from(round)
//                .where(sectionIdEq((long) sectionId))
//                .fetch();
//    }
//
//    private BooleanExpression sectionIdEq(Long sectionId) {
//        return sectionId != null ? section.id.eq(sectionId) : null;
//    }

}
