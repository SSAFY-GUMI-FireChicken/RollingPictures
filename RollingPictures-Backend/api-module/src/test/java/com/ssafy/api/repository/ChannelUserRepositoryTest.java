package com.ssafy.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class ChannelUserRepositoryTest {

    @Autowired
    ChannelUserRepository channelUserRepository;

    @Autowired
    EntityManager em;


    @Test
    void JPA_테스트() {

    }
}
