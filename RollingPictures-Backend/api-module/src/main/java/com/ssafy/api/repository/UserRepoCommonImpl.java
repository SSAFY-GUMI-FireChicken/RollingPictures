package com.ssafy.api.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.api.domain.QUser;
import com.ssafy.api.domain.User;
import com.ssafy.core.code.JoinCode;
import com.ssafy.core.code.YNCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional
public class UserRepoCommonImpl implements UserRepoCommon {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public User findUserLogin(String uid, JoinCode type){
        User result = queryFactory
                .select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.uid.eq(uid), QUser.user.joinType.eq(type), QUser.user.isBind.eq(YNCode.Y))
                .fetchFirst();

        return result;
    }

    @Override
    public User findByUid(String uid, YNCode isBind){
        User result = queryFactory
                .select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.uid.eq(uid), checkUserIsBind(isBind))
                .fetchOne();

        return result;
    }

    // isBind 조건만 체크
    public BooleanExpression checkUserIsBind(YNCode isBind){
        if (isBind == null) {
            return null;
        }
        return QUser.user.isBind.eq(isBind);
    }
}





