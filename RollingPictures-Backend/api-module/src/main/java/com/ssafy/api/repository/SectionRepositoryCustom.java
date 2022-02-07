package com.ssafy.api.repository;

import com.ssafy.api.domain.Round;
import com.ssafy.api.domain.Section;
import com.ssafy.api.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepositoryCustom {
    List<Section> findSectionByGameChannelId(Long gameChannelId);

    Optional<Section> findSection(Long gameChannelId, Integer startOrder);

    Optional<Section> findSection(Long gameChannelId, Long userId);

    List<User> findOrder(Long gameChannelId);

    List<Round> findRounds(Long sectionId);
}
