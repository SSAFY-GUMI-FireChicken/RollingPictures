package com.ssafy.core.repository;

import com.ssafy.core.entity.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestRepo {
    List<Test> findAll(String query, Pageable pageable);

}
