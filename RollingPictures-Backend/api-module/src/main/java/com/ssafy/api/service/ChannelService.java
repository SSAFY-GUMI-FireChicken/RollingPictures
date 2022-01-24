package com.ssafy.api.service;

import com.ssafy.api.domain.Channel;
import com.ssafy.api.repository.ChannelRepositoty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {
    private final ChannelRepositoty channelRepositoty;

    /**
     * code로 방 조회
     * @param code
     * @return channel
     */
    public Channel findByCode(String code) {
        return channelRepositoty.findTopByCode(code);
    }

    /**
     * 방 생성 후 id 리턴
     * @param channel
     * @return id
     */
    @Transactional(readOnly = false)
    public Long saveChannel(Channel channel) {
        Channel resultChannel = channelRepositoty.save(channel);
        return resultChannel.getId();
    }

    /**
     * 방 삭제
     * @param channel
     */
    @Transactional(readOnly = false)
    public void deleteChannel(Channel channel) {
        channelRepositoty.delete(channel);
    }
}
