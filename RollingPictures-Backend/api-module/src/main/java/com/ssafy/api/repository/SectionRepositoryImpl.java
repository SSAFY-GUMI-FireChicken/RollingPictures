package com.ssafy.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
@Transactional
public class SectionRepositoryImpl implements SectionRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


}
