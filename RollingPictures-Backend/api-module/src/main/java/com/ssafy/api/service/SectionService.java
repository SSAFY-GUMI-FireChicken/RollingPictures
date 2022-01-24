package com.ssafy.api.service;

import com.ssafy.api.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {

    private SectionRepository sectionRepository;
}
