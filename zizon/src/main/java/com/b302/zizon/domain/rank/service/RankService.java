package com.b302.zizon.domain.rank.service;

import com.b302.zizon.domain.rank.dto.RankRequestDTO;
import com.b302.zizon.domain.rank.entity.UserRank;
import com.b302.zizon.domain.rank.repository.RankRepository;
import com.b302.zizon.domain.user.GetUser;
import com.b302.zizon.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankService {

    private final GetUser getUser;
    private final RankRepository rankRepository;


    // 랭크 저장
    @Transactional
    public Map<String, Object> saveRank(RankRequestDTO requestDTO){

        User user = getUser.getUser();

        Optional<UserRank> byUserUserId = rankRepository.findByUserUserId(user.getUserId());

        if(byUserUserId.isEmpty()){
            UserRank build = UserRank.builder()
                    .record(requestDTO.getRecord())
                    .user(user)
                    .build();

            rankRepository.save(build);
        }else{
            UserRank userRank = byUserUserId.get();

            if(userRank.getRecord() > requestDTO.getRecord()){
                userRank.updateRecord(requestDTO.getRecord());
            }
        }

        Map<String, Object> result = new HashMap<>();

        result.put("message", "랭크 저장 성공");
        return result;
    }

     //기록 가져오기
//    public Map<String, Object> getRanking(){
//        User user = getUser.getUser();
//
//        List<UserRank> top10ByRecord = rankRepository.findTop10ByRecord();
//    }
}
