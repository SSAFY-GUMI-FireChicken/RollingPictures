package com.ssafy.api.service;

import com.ssafy.api.domain.*;
import com.ssafy.api.dto.req.SectionCreateReqDTO;
import com.ssafy.api.dto.res.SectionCreateResDTO;
import com.ssafy.api.dto.res.SectionResDTO;
import com.ssafy.api.repository.ChannelUserRepository;
import com.ssafy.api.repository.GameChannelRepository;
import com.ssafy.api.repository.GameChannelUserOrderRepository;
import com.ssafy.api.repository.SectionRepository;
import com.ssafy.core.exception.ApiMessageException;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionRepository sectionRepository;
    private final GameChannelRepository gameChannelRepository;
    private final ChannelUserRepository channelUserRepository;

    public List<SectionResDTO> getSection(Long gameChannelId, Long userId) {
        // 1. 게임방ID와 유저ID를 이용해 해당 유저의 시작 번호를 구합니다.
        GameChannel findGameChannel = gameChannelRepository.findGameChannelById(gameChannelId)
                .orElseThrow(() -> new ApiMessageException("잘못된 게임방 정보입니다."));

        int startOrder = 0;
        for (GameChannelUserOrder gameChannelUserOrder : findGameChannel.getGameChannelUserOrders()) {
            Long curUserId = gameChannelUserOrder.getUser().getId();
            if (curUserId == userId) {
                startOrder = gameChannelUserOrder.getOrderNum();
                break;
            }
        }

        // 2. 해당 게임방의 여러 섹션 중 위에서 구한 '시작 번호'가 같은 섹션을 찾아 값을 세팅합니다.
        List<SectionResDTO> list = new ArrayList<>();
        for (Section section : findGameChannel.getSections()) {
            if (section.getStartOrder() == startOrder) {
                for (Round round : section.getRounds()) {
                    list.add(SectionResDTO.builder()
                            .num(round.getRoundNumber())
                            .keyword(round.getKeyword())
                            .img(round.getImgSrc())
                            .userId(round.getUser().getId())
                            .build());
                }
            }
        }

        return new ArrayList<>();
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
