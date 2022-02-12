package com.ssafy.api.service;

import com.ssafy.api.domain.Progress;
import com.ssafy.api.repository.ProgressRepository;
import com.ssafy.core.code.ProgressCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;

    @Transactional(readOnly = false)
    public ProgressCode isNextRound(int roundNumber, Long gameChannelId) {
        Progress progress =  progressRepository.findByGameChannel_Id(gameChannelId);

        if (progress.getGameChannel().getCurRoundNumber() != roundNumber) {
            return ProgressCode.NONE;
        }

        progress.changeDonePeopleCnt(progress.getDonePeopleCnt() + 1);

        progressRepository.save(progress);

        if (progress.getDonePeopleCnt() == progress.getGameChannel().getConPeopleCnt()) {
          progress.changeDonePeopleCnt(0);
          progress.changeStartDate();
          progress.getGameChannel().addCurRoundNumber();

          if (progress.getGameChannel().getChannel().getCurPeopleCnt() < progress.getGameChannel().getCurRoundNumber()) {
              return ProgressCode.END;
          }
          return ProgressCode.NEXT;
        } else {
          return ProgressCode.NONE;
        }
    }
}
