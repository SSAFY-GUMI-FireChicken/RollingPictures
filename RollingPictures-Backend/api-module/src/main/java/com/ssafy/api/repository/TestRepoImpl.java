package com.ssafy.api.repository;

import com.querydsl.core.BooleanBuilder;
import com.ssafy.api.domain.QTest;
import com.ssafy.api.domain.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

public class TestRepoImpl extends QuerydslRepositorySupport implements TestRepo {
    private QTest test = QTest.test;

    public TestRepoImpl(){

        super(Test.class);
    }

    @Override
    public List<Test> findAll(String query, Pageable pageable){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!StringUtils.isEmpty(query)) {
            booleanBuilder.and(
                    test.title.containsIgnoreCase(query)
                            .or(test.content.containsIgnoreCase(query)));

        }
        return getQuerydsl().applyPagination(pageable,from(test))
                .where(booleanBuilder)
                .fetch();
    }
}
