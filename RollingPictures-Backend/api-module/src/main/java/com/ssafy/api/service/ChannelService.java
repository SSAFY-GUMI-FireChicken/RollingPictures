package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.dto.req.MakeChannelReqDTO;
import com.ssafy.api.dto.res.ChannelListResDTO;
import com.ssafy.api.dto.res.ChannelResDTO;
import com.ssafy.api.repository.ChannelRepository;
import com.ssafy.core.code.YNCode;
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

        result.setTotalPageNum(channelRepository.countByIsPublicAndIsPlaying(YNCode.Y, YNCode.N));
        result.setChannels(list);

        return result;
    }
}
