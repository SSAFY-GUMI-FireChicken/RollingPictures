package com.ssafy.api.repository;

import com.ssafy.api.domain.Section;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepositoryCustom {
    List<Section> findSectionByGameChannelId(Long gameChannelId);
}
