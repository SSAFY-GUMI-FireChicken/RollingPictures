package com.ssafy.api.service;

import com.ssafy.api.domain.Progress;
import com.ssafy.api.repository.ProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;

    @Transactional(readOnly = false)
        public boolean isNextRound(int roundNumber, Long gameChannelId) {
        Progress progress =  progressRepository.findByGameChannel_Id(gameChannelId);

        if (progress.getGameChannel().getCurRoundNumber() != roundNumber) {
            return false;
        }

        progress.changeDonePeopleCnt(progress.getDonePeopleCnt() + 1);

        progressRepository.save(progress);

        if (progress.getDonePeopleCnt() == progress.getGameChannel().getConPeopleCnt()) {
          progress.changeDonePeopleCnt(0);
          progress.changeStartDate();
          progress.getGameChannel().addCurRoundNumber();

          return true;
        } else {
          return false;
        }
    }
}
