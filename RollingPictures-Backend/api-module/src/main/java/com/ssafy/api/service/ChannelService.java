package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.repository.ChannelRepository;
import com.ssafy.core.code.YNCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Channel createChannel() {
        Channel channelChk = null;
        String code = "";

        do {
            code = RandomStringUtils.randomAlphanumeric(6);
            channelChk = findByCode(code);
        } while (channelChk != null);

        Channel channel = Channel.builder()
                .maxPeopleCnt(6)
                .curPeopleCnt(0)
                .isPlaying(YNCode.N)
                .code(code)
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
}
