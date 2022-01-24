package com.ssafy.api.service;

import com.ssafy.api.domain.Test;
import com.ssafy.api.repository.TestRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {

        this.testRepository = testRepository;
    }

    public List<Test> findAll(String query, Pageable pageable) {

        return testRepository.findAll(query, pageable);
    }

    @Transactional(readOnly = false)
    public long post(Test test) {
        Test postTest = testRepository.save(test);
        return postTest.getId();
    }

    public Optional<Test> findById(Long testSeq) {

        return testRepository.findById(testSeq);
    }

    public void delete(Long testSeq) {
        testRepository.deleteById(testSeq);
    }
}
