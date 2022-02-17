package com.ssafy.api.service;

import com.ssafy.api.domain.*;
import com.ssafy.api.dto.req.InOutChannelReqDTO;
import com.ssafy.api.dto.res.UserInfoResDTO;
import com.ssafy.api.repository.ChannelUserRepository;
import com.ssafy.api.repository.GameChannelUserInfoRepository;
import com.ssafy.core.code.GamePlayState;
import com.ssafy.core.code.YNCode;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelUserService {
    private final ChannelUserRepository channelUserRepository;
    private final ChannelService channelService;
    private final SocketService socketService;
    private final GameChannelService gameChannelService;
    private  final GameChannelUserInfoRepository gameChannelUserInfoRepository;

    /**
     * channelUser 생성 후 리턴
     * @param channel, user, YNCode
     * @return ArrayList<UserInfoResDTO>
     */
    @Transactional(readOnly = false)
    public ArrayList<UserInfoResDTO> createChannelUser(Channel channel, User user, YNCode isLeader) {
        ChannelUser channelUser = ChannelUser.builder()
                .channel(channel)
                .user(user)
                .isLeader(isLeader)
                .gamePlayState(GamePlayState.NONE)
                .isMute(YNCode.N)
                .build();

        channel.changeCurPeopleCnt(1);
        channel.addChannelUser(channelUser);

        channelUserRepository.save(channelUser);

        socketService.sendInChannelUser(channel.getCode(), UserInfoResDTO
                .builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .isLeader(channelUser.getIsLeader())
                .build()
        );

        ArrayList<UserInfoResDTO> resUserList = new ArrayList<>();

        channel.getChannelUsers().forEach(x -> resUserList.add(UserInfoResDTO
                .builder()
                .id(x.getUser().getId())
                .nickname(x.getUser().getNickname())
                .isLeader(x.getIsLeader())
                .build()));

        return resUserList;
    }

    /**
     * user로 channelUser 조회
     * @param user
     * @return channelUser
     */
    public ChannelUser findByUser(User user) {
        return channelUserRepository.findByUser(user);
    }

    /**
     * channelUser 삭제
     * @param channelUser
     */
    @Transactional(readOnly = false)
    public void deleteChannelUser(ChannelUser channelUser) {
        if (channelUser.getChannel().getCurPeopleCnt() == 1) {
            Long channelId = channelUser.getChannel().getId();
            gameChannelService.deleteGameChannel(channelId);
            channelService.deleteChannel(channelUser.getChannel());
        } else {
            channelUser.getChannel().getChannelUsers().remove(channelUser);

            if (channelUser.getIsLeader() == YNCode.Y) {
                ChannelUser newLeader = channelUser.getChannel().getChannelUsers().get(0);
                newLeader.changeIsLeader(YNCode.Y);

                socketService.sendChangingLeader(newLeader);
            }

            socketService.sendOutChannelUser(channelUser);

            channelUser.getChannel().changeCurPeopleCnt(-1);
        }

        channelUserRepository.delete(channelUser);
    }

    /**
     * sessionId update
     * @param userId, sessionId
     * @return channelUser
     */
    @Transactional(readOnly = false)
    public ChannelUser changeSessionId(Long userId, String sessionId) {
       ChannelUser channelUser = channelUserRepository.findByUser_Id(userId);
       channelUser.changeSessionId(sessionId);

        if (channelUser.getChannel().getIsPlaying() == YNCode.Y) {
            channelUser.getChannel().getGameChannel().changeConPeopleCnt(1);

            GameChannelUserInfo gameChannelUserInfo = gameChannelUserInfoRepository.findByUser_Id(userId);

            if (gameChannelUserInfo.getSubmitRoundNum() == gameChannelUserInfo.getGameChannel().getCurRoundNumber()) {
                gameChannelUserInfo.getGameChannel().changeDonePeopleCnt(gameChannelUserInfo.getGameChannel().getDonePeopleCnt() + 1);
                gameChannelUserInfoRepository.save(gameChannelUserInfo);
            }
        }

        return channelUserRepository.save(channelUser);
    }

    /**
     * sessionId update
     * @param sessionId
     * @return
     */
    @Transactional(readOnly = false)
    public void removeSessionId(String sessionId) {
        ChannelUser channelUser = channelUserRepository.findBySessionId(sessionId);

        if (channelUser == null) {
            return;
        }

        channelUser.changeSessionId(null);

        if (channelUser.getChannel().getIsPlaying() == YNCode.Y) {
            channelUser.getChannel().getGameChannel().changeConPeopleCnt(-1);

            if (channelUser.getChannel().getGameChannel().getConPeopleCnt() == 0) {
                channelService.deleteChannel(channelUser.getChannel());
            } else {
                GameChannelUserInfo gameChannelUserInfo = gameChannelUserInfoRepository.findByUser_Id(channelUser.getUser().getId());

                if (gameChannelUserInfo.getSubmitRoundNum() == channelUser.getChannel().getGameChannel().getCurRoundNumber()) {
                    gameChannelUserInfo.getGameChannel().changeDonePeopleCnt(gameChannelUserInfo.getGameChannel().getDonePeopleCnt() - 1);
                } else {
                    if (channelUser.getChannel().getGameChannel().getConPeopleCnt() <= gameChannelUserInfo.getGameChannel().getDonePeopleCnt()) {
                        channelUser.getChannel().getGameChannel().changeDonePeopleCnt(0);
                        channelUser.getChannel().getGameChannel().changeStartDate();
                        channelUser.getChannel().getGameChannel().addCurRoundNumber();

                        if (channelUser.getChannel().getGameChannel().getCurRoundNumber() > channelUser.getChannel().getCurPeopleCnt()) {
                            socketService.sendGameEnd(channelUser.getChannel().getCode(), channelUser.getChannel().getGameChannel().getCurRoundNumber());
                            channelService.deleteDisconnectChannelUsers(channelUser.getChannel().getGameChannel().getId());
                        } else {
                            socketService.sendNextSignal(channelUser.getChannel().getCode(), channelUser.getChannel().getGameChannel().getCurRoundNumber());
                        }
                    } else {
                        System.out.println(channelUser.getChannel().getGameChannel().getConPeopleCnt()+" "+gameChannelUserInfo.getGameChannel().getDonePeopleCnt());
                    }
                }

                gameChannelUserInfoRepository.save(gameChannelUserInfo);
                channelUserRepository.save(channelUser);
            }
        } else {
            channelUserRepository.save(channelUser);
        }
    }

    /**
     * 유저별 게임 진행 상태 변경
     * @param inOutChannelReqDTO
     */
    @Transactional(readOnly = false)
    public void changeChannelUserStatus(InOutChannelReqDTO inOutChannelReqDTO) {
        ChannelUser channelUser = channelUserRepository.findByUser_Id(inOutChannelReqDTO.getId());

        if (channelUser == null) {
            throw new ApiMessageException("방에 입장하지 않은 상태입니다.");
        } else if (!channelUser.getChannel().getCode().equals(inOutChannelReqDTO.getCode())) {
            throw new ApiMessageException("입장한 방이 아닙니다.");
        }

        channelUser.changeGamePlayState(GamePlayState.NONE);
        if (channelUserRepository.save(channelUser) == null) {
            throw new ApiMessageException("채널 유저 상태 변경에 실패하였습니다.");
        }
    }
}
