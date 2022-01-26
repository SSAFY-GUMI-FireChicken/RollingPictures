//package com.ssafy.api.repository;
//
//import com.ssafy.api.domain.Channel;
//import com.ssafy.api.domain.Section;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//@SpringBootTest
//@Transactional
//public class SectionRepositoryTest {
//
//    @Autowired
//    SectionRepository sectionRepository;
//
//    @Autowired
//    EntityManager em;
//
//    @Test
//    void JPA_테스트() {
//        // given
//        Channel channel = new Channel(1L, 5, 3, "", "");
//        Section section = new Section(0L, "홍길동");
//        Section save = sectionRepository.save(section);
//
//        // when
//        Section result = sectionRepository.findById(save.getId())
//                .orElseGet(() -> new Section(0L, ""));
//
//        // then
//        Assertions.assertThat(result.getId()).isEqualTo(save.getId());
//    }
//}
