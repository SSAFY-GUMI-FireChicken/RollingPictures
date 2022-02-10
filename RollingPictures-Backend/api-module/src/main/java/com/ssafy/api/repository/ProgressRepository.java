package com.ssafy.api.repository;

import com.ssafy.api.domain.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Progress findByGameChannel_Id(Long gameChannelId);
}
