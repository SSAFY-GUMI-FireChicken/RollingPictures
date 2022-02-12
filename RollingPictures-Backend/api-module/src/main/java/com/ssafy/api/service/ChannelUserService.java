package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.domain.User;
import com.ssafy.api.dto.res.UserInfoResDTO;
import com.ssafy.api.repository.ChannelUserRepository;
import com.ssafy.core.code.GamePlayState;
import com.ssafy.core.code.YNCode;
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

                socketService.sendChangingLeader(channelUser);
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
            channelUserRepository.save(channelUser);
        } else {
            deleteChannelUser(channelUser);
        }
    }
}
