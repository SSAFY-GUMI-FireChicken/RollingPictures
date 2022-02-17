package com.ssafy.api.service;

import com.ssafy.api.domain.GameChannel;
import com.ssafy.api.domain.GameChannelUserInfo;
import com.ssafy.api.repository.GameChannelUserInfoRepository;
import com.ssafy.core.code.ProgressCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GameChannelUserInfoService {
    private final GameChannelUserInfoRepository gameChannelUserInfoRepository;

    @Transactional(readOnly = false)
    public ProgressCode isNextRound(int roundNumber, Long userId) {
        GameChannelUserInfo gameChannelUserInfo = gameChannelUserInfoRepository.findByUser_Id(userId);
        GameChannel gameChannel = gameChannelUserInfo.getGameChannel();
        System.out.println("어ㅐ");

        if (gameChannel.getCurRoundNumber() != roundNumber || gameChannelUserInfo.getSubmitRoundNum() >= roundNumber) {
            System.out.println(roundNumber);

            return ProgressCode.NONE;
        }

        gameChannelUserInfo.changeSubmitRoundNum(roundNumber);
        gameChannel.changeDonePeopleCnt(gameChannel.getDonePeopleCnt() + 1);

        gameChannelUserInfoRepository.save(gameChannelUserInfo);

        if (gameChannel.getDonePeopleCnt() >= gameChannel.getConPeopleCnt()) {
            gameChannel.changeDonePeopleCnt(0);
            gameChannel.changeStartDate();
            gameChannel.addCurRoundNumber();

            if (gameChannel.getChannel().getCurPeopleCnt() < gameChannel.getCurRoundNumber()) {
                return ProgressCode.END;
            }
            return ProgressCode.NEXT;
        } else {
            return ProgressCode.NONE;
        }
    }
}
