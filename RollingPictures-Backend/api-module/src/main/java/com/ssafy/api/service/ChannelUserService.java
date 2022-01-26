package com.ssafy.api.service;

import com.ssafy.api.domain.ChannelUser;
import com.ssafy.api.domain.User;
import com.ssafy.api.repository.ChannelUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelUserService {
    private final ChannelUserRepository channelUserRepository;

    /**
     * channelUser 생성 후 리턴
     * @param channelUser
     * @return id
     */
    @Transactional(readOnly = false)
    public ChannelUser saveChannelUser(ChannelUser channelUser) {
        return channelUserRepository.save(channelUser);
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
        channelUserRepository.delete(channelUser);
    }

}
