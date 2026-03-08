package com.apt.user.mapper;

import com.apt.auth.dto.request.UserSignUpReq;
import com.apt.user.dto.request.UpdateUserReq;
import com.apt.user.dto.response.UserGetMeRes;
import com.apt.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// user 테이블 접근 MyBatis Mapper
@Mapper
public interface UserMapper {

    // 회원 등록 (일반 회원가입)
    int signUp(UserSignUpReq req);

    // 이메일로 사용자 조회 (로그인, 중복 체크용)
    User findByEmail(String email);

    // userId로 사용자 조회 (마이페이지용)
    UserGetMeRes findById(Long userId);

    // 소셜 로그인 제공자 + 제공자 ID로 사용자 조회
    User findByProviderAndProviderId(String provider, String providerId);

    // 소셜 로그인 신규 사용자 등록
    int signUpOAuth(User user);

    // 소프트 딜리트 (is_deleted=1, deleted_at=NOW())
    int softDeleteUser(Long userId);

    // 소셜 로그인 후 동호수 연결 + status APPROVED 처리
    int linkHousehold(@Param("userId") Long userId,
                      @Param("householdId") Long householdId,
                      @Param("phone") String phone);

    // 사용자 정보 수정 (이름, 전화번호)
    int updateUser(@Param("userId") Long userId, @Param("req") UpdateUserReq req);
}