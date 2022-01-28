package com.ssafy.api.service;

import com.ssafy.api.domain.Round;
import com.ssafy.api.repository.RoundRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundService(RoundRepository roundRepository) {

        this.roundRepository = roundRepository;
    }

    @Transactional(readOnly = false)
    public long post(Round round) {
        Round postRound = roundRepository.save(round);
        return postRound.getId();
    }


    public void delete(Long testround) {

        roundRepository.deleteById(testround);
    }
}

