package com.ssafy.core.repository;

import com.ssafy.core.entity.Test;
import com.ssafy.core.repository.TestRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long>,TestRepo {
}
