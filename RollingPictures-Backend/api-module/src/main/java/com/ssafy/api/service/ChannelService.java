package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.dto.req.MakeChannelReqDTO;
import com.ssafy.api.dto.res.ChannelListResDTO;
import com.ssafy.api.dto.res.ChannelResDTO;
import com.ssafy.api.dto.res.UserInfoResDTO;
import com.ssafy.api.repository.ChannelRepository;
import com.ssafy.core.code.GamePlayState;
import com.ssafy.core.code.YNCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final SocketService socketService;

    /**
     * code로 방 조회
     * @param code
     * @return channel
     */
    public Channel findByCode(String code) {
        return channelRepository.findTopByCode(code);
    }

    /**
     * 방 생성 후 channel 리턴
     * @return channel
     */
    @Transactional(readOnly = false)
    public Channel createChannel(MakeChannelReqDTO req) {
        Channel channelChk = null;
        String code = "";

        do {
            code = RandomStringUtils.randomAlphanumeric(6);
            channelChk = findByCode(code);
        } while (channelChk != null);

        Channel channel = Channel.builder()
                .maxPeopleCnt(req.getMaxPeopleCnt())
                .curPeopleCnt(0)
                .isPlaying(YNCode.N)
                .code(code)
                .isPublic(req.getIsPublic())
                .title(req.getTitle())
                .build();

        return channelRepository.save(channel);
    }

    /**
     * 방 삭제
     * @param channel
     */
    @Transactional(readOnly = false)
    public void deleteChannel(Channel channel) {
        channelRepository.delete(channel);
    }

    /**
     * 방 목록 조회
     * @return List
     */
    @Transactional(readOnly = false)
    public ChannelListResDTO getChannelList(int page, int batch) {
        ChannelListResDTO result = ChannelListResDTO.builder().build();

        List<Channel> channels = channelRepository.findByIsPublicAndIsPlaying(
                YNCode.Y,
                YNCode.N,
                PageRequest.of(page, batch)
        );

        List<ChannelResDTO> list = new ArrayList<>();

        channels.forEach(x -> list.add(ChannelResDTO
                .builder()
                .id(x.getId())
                .code(x.getCode())
                .title(x.getTitle())
                .maxPeopleCnt(x.getMaxPeopleCnt())
                .curPeopleCnt(x.getCurPeopleCnt())
                .isPublic(x.getIsPublic())
                .build()
        ));

        result.setTotalCnt(channelRepository.countByIsPublicAndIsPlaying(YNCode.Y, YNCode.N));
        result.setChannels(list);

        return result;
    }


    /**
     * 채널 상태 변경 & 연결되지 않은 유저 자동 퇴장
     * @param
     * @return
     */
    @Transactional(readOnly = false)
    public void deleteUnconnectChannelUsers(Long gameChannelId) {
        Channel channel = channelRepository.findByGameChannel_Id(gameChannelId);

        channel.changeIsPlaying(YNCode.N);
        List<ChannelUser> users = channel.getChannelUsers();

        boolean isDeletedLeader = false;
        ChannelUser newLeader = null;

        Iterator<ChannelUser> iter = users.iterator();

        while (iter.hasNext()) {
            ChannelUser channelUser = iter.next();

            if (channelUser.getSessionId() == null || channelUser.getSessionId().equals(null)) {
                iter.remove();

                if (channelUser.getIsLeader() == YNCode.Y) {
                    isDeletedLeader = true;
                    channelUser.getChannel().changeCurPeopleCnt(-1);

                    socketService.sendOutChannelUser(channelUser);
                }
            } else if (isDeletedLeader) {
                isDeletedLeader = false;
                newLeader = channelUser;
                newLeader.changeIsLeader(YNCode.Y);
            }
        }

        if (newLeader != null) {
            socketService.sendChangingLeader(newLeader);
        }

        channelRepository.save(channel);
    }
}
