package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.domain.User;
import com.ssafy.api.dto.req.InOutChannelReqDTO;
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

        channel.setCurPeopleCnt(channel.getCurPeopleCnt() + 1);
        channel.addChannelUser(channelUser);

        channelUserRepository.save(channelUser);

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
            channelService.deleteChannel(channelUser.getChannel());
            channelUserRepository.delete(channelUser);
        } else {
            channelUser.getChannel().getChannelUsers().remove(channelUser);

            if (channelUser.getIsLeader() == YNCode.Y) {
                channelUser.getChannel().getChannelUsers().get(0).setIsLeader(YNCode.Y);
            }

            channelUser.getChannel().setCurPeopleCnt(channelUser.getChannel().getCurPeopleCnt() - 1);
            channelUserRepository.delete(channelUser);
        }
    }
}
