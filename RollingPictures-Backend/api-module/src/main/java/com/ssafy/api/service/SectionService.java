//package com.ssafy.api.service;
//
//import com.ssafy.api.domain.GameChannelUserOrder;
//import com.ssafy.api.domain.Round;
//import com.ssafy.api.domain.Section;
//import com.ssafy.api.dto.res.SectionResDTO;
//import com.ssafy.api.repository.GameChannelRepository;
//import com.ssafy.api.repository.GameChannelUserOrderRepository;
//import com.ssafy.api.repository.SectionRepository;
//import lombok.RequiredArgsConstructor;
//import org.hibernate.collection.internal.PersistentBag;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//
//@RequiredArgsConstructor
//@Service
//@Transactional(readOnly = true)
//public class SectionService {
//    private final SectionRepository sectionRepository;
//    private final GameChannelUserOrderRepository gameChannelUserOrderRepository;
//
//    public List<SectionResDTO> getSection(Long gameChannelId, Long userId) {
//        List<GameChannelUserOrder> gameChannelUserOrder =
//                gameChannelUserOrderRepository.findAllUser(gameChannelId).orElseGet(ArrayList::new);
//        int startOrder = 0;
//        for (GameChannelUserOrder channelUserOrder : gameChannelUserOrder) {
//            if (channelUserOrder.getUser().getId() == userId) {
//                startOrder = channelUserOrder.getOrderNum();
//                break;
//            }
//        }
//        Section findSection =
//                sectionRepository.findByStartOrderAndGameChannelId(startOrder, gameChannelId).orElseGet(Section::new);
//
//        List<SectionResDTO> list = new ArrayList<>();
//        int num = findSection.getStartOrder();
//        findSection.getRounds().forEach(x -> list.add(
//                new SectionResDTO(num, x.getUser().getId(), x.getImgSrc(), x.getKeyword()))
//        );
//        return list;
//    }
//}
