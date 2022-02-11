package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.dto.req.MakeChannelReqDTO;
import com.ssafy.api.dto.res.ChannelListResDTO;
import com.ssafy.api.dto.res.ChannelResDTO;
import com.ssafy.api.repository.ChannelRepository;
import com.ssafy.api.repository.ChannelUserRepository;
import com.ssafy.core.code.YNCode;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelUserRepository channelUserRepository;
    private final SocketService socketService;

    /**
     * code로 방 조회
     *
     * @param code
     * @return channel
     */
    public Channel findByCode(String code) {
        return channelRepository.findTopByCode(code);
    }

    /**
     * 방 생성 후 channel 리턴
     *
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
     *
     * @param channel
     */
    @Transactional(readOnly = false)
    public void deleteChannel(Channel channel) {
        channelRepository.delete(channel);
    }

    /**
     * 방 목록 조회
     *
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

    @Transactional(readOnly = false)
    public ChannelResDTO changeChannelOption(MakeChannelReqDTO req) throws ApiMessageException {
        ChannelUser channelUser = channelUserRepository.findByUser_Id(req.getId());

        if (channelUser == null) {
            throw new ApiMessageException("방에 입장한 상태가 아닙니다.");
        } else if (channelUser.getIsLeader() == YNCode.N) {
            throw new ApiMessageException("방장만 변경할 수 있습니다.");
        }

        Channel channel = channelUser.getChannel();

        channel.changeTitle(req.getTitle());
        channel.changeIsPublic(req.getIsPublic());

        if (req.getMaxPeopleCnt() < channel.getCurPeopleCnt()) {
            throw new ApiMessageException("최대 인원을 현재 인원보다 적게 변경할 수 없습니다.");
        } else {
            channel.changeMaxPeopleCnt(req.getMaxPeopleCnt());
        }

        Channel channelChk = channelRepository.save(channel);
        if (channelChk == null) {
            throw new ApiMessageException("방 옵션 변경에 실패하였습니다.");
        } else {
            ChannelResDTO channelResDTO = ChannelResDTO.builder()
                    .id(channel.getId())
                    .code(channel.getCode())
                    .isPublic(channel.getIsPublic())
                    .curPeopleCnt(channel.getCurPeopleCnt())
                    .maxPeopleCnt(channel.getMaxPeopleCnt())
                    .title(channel.getTitle())
                    .users(channel.changeToUserInfo())
                    .build();

            socketService.sendChangingOption(channelResDTO);
            return channelResDTO;
        }
    }
}
