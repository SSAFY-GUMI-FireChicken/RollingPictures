package com.ssafy.api.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.*;
import com.ssafy.core.code.JoinCode;
import com.ssafy.core.code.YNCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class RoundRepoImpl implements RoundRepoCommon {
    private final JPAQueryFactory queryFactory;
    private EntityManager em;

    public RoundRepoImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }



}
