package com.b302.zizon.util.kakaoAPI.controller;

import com.b302.zizon.domain.refreshtoken.entity.RefreshToken;
import com.b302.zizon.domain.refreshtoken.repository.RefreshTokenRepository;
import com.b302.zizon.domain.user.entity.User;
import com.b302.zizon.domain.user.repository.UserRepository;
import com.b302.zizon.util.jwt.JwtUtil;
import com.b302.zizon.util.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {

    @Value("${jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("oauth/login")
    public DataResponse<Map<String, Object>> Login(@RequestBody Map<String, Object> data) throws IOException {
        // 암호화 시켜놓은 access를 받음
        String access = (String) data.get("access");

        Optional<User> optionalUser = userRepository.findByPrivateAccess(access);

        if (optionalUser.isEmpty()) {
            // 유저 정보가 저장되지 않음(에러처리)
        }
        User UserInfo = optionalUser.get();

        // 로그인 성공
        String accessToken = JwtUtil.createAccessJwt(UserInfo.getUserId(), secretKey); // 토큰 발급해서 넘김
        String refreshToken = JwtUtil.createRefreshToken(secretKey); // 리프레시 토큰 발급해서 넘김


//        refreshTokenRepository.save(new RefreshToken(String.valueOf(UserInfo.getUserId()), refreshToken, accessToken));

        Map<String, Object> result = new HashMap<>();

        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("accountType", UserInfo.getAccountType());
        result.put("nickname", UserInfo.getNickname());
        result.put("profileImage", UserInfo.getProfileImage());
        result.put("userId", UserInfo.getUserId());


//        Optional<RefreshToken> byUserUserNo = refreshTokenRepository.findByUserUserNo(UserInfo.getUserNo());
//
//        if(byUserUserNo.isPresent()){
//            RefreshToken currentRefreshToken = byUserUserNo.get(); // 현재 유저의 리프레시 토큰 꺼내옴
//
//            currentRefreshToken.setRefreshToken(refreshToken); // 전부 새롭게 저장
//            currentRefreshToken.setExpirationDate(LocalDateTime.now().plusDays(14));
//            currentRefreshToken.setCreateDate(LocalDateTime.now());
//
//            refreshTokenRepository.save(currentRefreshToken); // db에 저장
//
//        } else {
//            RefreshToken refreshTokenUser = RefreshToken.builder() // 리프레시 토큰 빌더로 생성
//                    .user(UserInfo)
//                    .refreshToken(refreshToken)
//                    .expirationDate(LocalDateTime.now().plusDays(14))
//                    .build();
//
//            refreshTokenRepository.save(refreshTokenUser); // 리프레시 토큰 저장.
//        }

        DataResponse<Map<String, Object>> response = null;

        if(UserInfo.getAccountType().equals("kakao")){
            response = new DataResponse<>(200, "카카오 로그인 성공");
        }else if(UserInfo.getAccountType().equals("naver")){
            response = new DataResponse<>(200, "네이버 로그인 성공");
        }

        System.out.println(response);
        response.setData(result);

        return response;
    }
}
