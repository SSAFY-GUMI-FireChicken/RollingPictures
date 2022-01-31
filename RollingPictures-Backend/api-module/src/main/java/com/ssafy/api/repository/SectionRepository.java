package com.ssafy.api.repository;

import com.ssafy.api.domain.Section;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long>, SectionRepositoryCustom {
    @EntityGraph(attributePaths = {"round"})
    Optional<Section> findByStartOrderAndGameChannelId(Integer startOrder, Long gameChannelId);

}
