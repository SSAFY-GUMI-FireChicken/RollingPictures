package com.ssafy.api.service;

import com.ssafy.api.domain.GameChannel;
import com.ssafy.api.domain.GameChannelUserOrder;
import com.ssafy.api.domain.Round;
import com.ssafy.api.domain.Section;
import com.ssafy.api.dto.res.SectionResDTO;
import com.ssafy.api.repository.GameChannelRepository;
import com.ssafy.api.repository.GameChannelUserOrderRepository;
import com.ssafy.api.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionRepository sectionRepository;
    private final GameChannelRepository gameChannelRepository;
    private final GameChannelUserOrderRepository gameChannelUserOrderRepository;

    public List<SectionResDTO> getSection(Long gameChannelId, Long userId) {
        // 1. 게임방ID와 유저ID를 이용해 해당 유저의 시작 번호를 구합니다.
        GameChannel findGameChannel = gameChannelRepository.findGameChannelById(gameChannelId);
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
}
