//package com.ssafy.api.repository;
//
//import com.ssafy.api.domain.Channel;
//import com.ssafy.api.domain.GameChannel;
//import com.ssafy.api.domain.User;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class GameChannelRepositoryTest {
//    @Autowired GameChannelRepository gameChannelRepository;
//    @Autowired UserRepository userRepository;
//    @Autowired ChannelRepository channelRepository;
//
//    @BeforeEach
//    void before() {
//        userRepository.save(User.builder().nickname("userA").build());
//        userRepository.save(User.builder().nickname("userB").build());
//        userRepository.save(User.builder().nickname("userC").build());
//
//        channelRepository.save(Channel.builder().curPeopleCnt(1).build());
//    }
//
//    @Test
//    void basicTest() {
////        GameChannel gameChannel = gameChannelRepository.save(GameChannel.builder()
////                .code("code")
////                .build());
////
////        GameChannel findGameChannel = gameChannelRepository.findGameChannelById(gameChannel.getId());
////        Assertions.assertThat(findGameChannel.getId()).isEqualTo(gameChannel.getId());
//
//        User findUser = userRepository.findByNickname("userA");
//        System.out.println("findUser = " + findUser);
//    }
//}
