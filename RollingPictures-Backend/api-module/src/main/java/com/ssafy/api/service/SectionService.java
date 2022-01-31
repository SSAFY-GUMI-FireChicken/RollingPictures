package com.ssafy.api.service;

import com.ssafy.api.domain.*;
import com.ssafy.api.dto.req.SectionCreateReqDTO;
import com.ssafy.api.dto.res.SectionCreateResDTO;
import com.ssafy.api.dto.res.SectionRetrieveResDTO;
import com.ssafy.api.repository.ChannelUserRepository;
import com.ssafy.api.repository.GameChannelRepository;
import com.ssafy.api.repository.GameChannelUserOrderRepository;
import com.ssafy.api.repository.SectionRepository;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionRepository sectionRepository;
    private final GameChannelRepository gameChannelRepository;
    private final ChannelUserRepository channelUserRepository;
    private final GameChannelUserOrderRepository gameChannelUserOrderRepository;

    public List<SectionRetrieveResDTO> getSection(Long gameChannelId, Long userId) throws Exception {
        GameChannel findGameChannel = gameChannelRepository.findById(gameChannelId)
                .orElseThrow(() -> new ApiMessageException("잘못된 게임방 정보입니다."));

        Integer orderNum = gameChannelUserOrderRepository.findOrderNum(gameChannelId, userId)
                .orElseThrow(() -> new ApiMessageException("잘못된 정보입니다."));

        Section section = sectionRepository.findSection(gameChannelId, orderNum)
                .orElseThrow(() -> new ApiMessageException("잘못된 정보입니다."));

        List<SectionRetrieveResDTO> result = new ArrayList<>();
        for (Round round : section.getRounds()) {
            result.add(SectionRetrieveResDTO.builder()
                    .sectionId(section.getId())
                    .userId(round.getUser().getId())
                    .roundNum(round.getRoundNumber())
                    .build());
        }
        return result;
    }

    @Transactional
    public List<SectionCreateResDTO> createSection(SectionCreateReqDTO dto) throws Exception {
        List<SectionCreateResDTO> result = new ArrayList<>();
        GameChannel findGameChannel = gameChannelRepository.findById(dto.getGameChannelId())
                .orElseThrow(() -> new ApiMessageException("잘못된 게임방 정보입니다."));

        Long channelId = findGameChannel.getChannel().getId();
        ChannelUser leader = channelUserRepository.findLeaderOfChannel(channelId);
        if (!Objects.equals(leader.getUser().getId(), dto.getUserId())) {
            throw new ApiMessageException("방장이 아닙니다.");
        }

        List<Section> sections = sectionRepository.findSectionByGameChannelId(dto.getGameChannelId());
        if (sections.size() > 0) {
            throw new ApiMessageException("이미 해당 게임방에 섹션이 생성되었습니다.");
        }

        for (GameChannelUserOrder gameChannelUserOrder : findGameChannel.getGameChannelUserOrders()) {
            Section section = Section.builder()
                    .gameChannel(findGameChannel)
                    .startOrder(gameChannelUserOrder.getOrderNum())
                    .code(findGameChannel.getCode())
                    .build();
            Section save = sectionRepository.save(section);
            result.add(SectionCreateResDTO.builder()
                    .id(save.getId())
                    .build());
        }
        return result;
    }
}
