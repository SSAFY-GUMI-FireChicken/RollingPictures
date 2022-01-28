package com.ssafy.api.repository;

import com.ssafy.api.domain.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TestRepo {
    List<Test> findAll(String query, Pageable pageable);

}
