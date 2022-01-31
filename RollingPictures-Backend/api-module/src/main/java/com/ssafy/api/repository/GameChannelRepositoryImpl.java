package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.ssafy.api.domain.QGameChannelUserOrder.*;

@RequiredArgsConstructor
@Repository
@Transactional
public class GameChannelRepositoryImpl implements GameChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
}
