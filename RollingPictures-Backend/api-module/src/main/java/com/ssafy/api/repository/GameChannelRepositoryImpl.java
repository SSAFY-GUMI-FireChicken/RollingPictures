package com.ssafy.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.GameChannel;
import com.ssafy.api.domain.QGameChannel;
import com.ssafy.api.domain.QGameChannelUserOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.ssafy.api.domain.QGameChannel.*;
import static com.ssafy.api.domain.QGameChannelUserOrder.*;

@RequiredArgsConstructor
@Repository
@Transactional
public class GameChannelRepositoryImpl implements GameChannelRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;


}
