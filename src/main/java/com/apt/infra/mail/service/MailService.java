package com.apt.infra.mail.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.infra.mail.dto.request.ResetPasswordReq;
import com.apt.infra.mail.mapper.PasswordResetTokenMapper;
import com.apt.infra.mail.model.PasswordResetToken;
import com.apt.user.mapper.UserMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

// 이메일 발송 및 비밀번호 재설정 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final PasswordResetTokenMapper passwordResetTokenMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 비밀번호 재설정 이메일 발송
    // 1. 이메일로 사용자 조회 → 2. 기존 토큰 삭제 → 3. 새 토큰 생성 → 4. 메일 발송
    @Transactional
    public void sendPasswordResetEmail(String email) {

        // 이메일로 사용자 조회, 없으면 예외
        var user = userMapper.findByEmail(email);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 기존 토큰 삭제 후 새 토큰 생성
        passwordResetTokenMapper.deleteByUserId(user.getUserId());

        // UUID로 토큰 생성
        String token = UUID.randomUUID().toString();

        // 토큰 저장 (30분 후 만료)
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getUserId());
        resetToken.setToken(token);
        resetToken.setExpiredAt(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenMapper.saveToken(resetToken);

        // 메일 발송
        sendEmail(email, token);
    }

    // 비밀번호 재설정 처리
    // 1. 토큰 조회 → 2. 만료 여부 확인 → 3. 비밀번호 변경 → 4. 토큰 삭제
    @Transactional
    public void resetPassword(ResetPasswordReq req) {

        // 토큰 조회
        PasswordResetToken resetToken = passwordResetTokenMapper.findByToken(req.getToken());
        if (resetToken == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰 만료 여부 확인
        if (resetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        // 새 비밀번호 암호화 후 변경
        String encodedPassword = passwordEncoder.encode(req.getNewPassword());
        userMapper.updatePassword(resetToken.getUserId(), encodedPassword);

        // 사용한 토큰 삭제
        passwordResetTokenMapper.deleteByUserId(resetToken.getUserId());
        log.info("비밀번호 재설정 완료 - userId: {}", resetToken.getUserId());
    }

    // 이메일 발송
    private void sendEmail(String toEmail, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[APTeN] 비밀번호 재설정 안내");
            helper.setText(buildEmailContent(resetToken), true);

            mailSender.send(message);
            log.info("비밀번호 재설정 메일 발송 완료 - to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("메일 발송 실패 - to: {}", toEmail, e);
            throw new CustomException(ErrorCode.MAIL_SEND_FAILED);
        }
    }

    // 이메일 HTML 내용 생성
    private String buildEmailContent(String resetToken) {
        String resetUrl = "http://localhost:5173/reset-password?token=" + resetToken;
        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head><body>"
                + "<div style='background:#f5f6fa; padding: 40px 0; font-family: \"Noto Sans KR\", sans-serif;'>"

                // 카드
                + "<div style='max-width:600px; margin:0 auto; background:#fff; border-radius:12px; overflow:hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.08);'>"

                // 헤더
                + "<div style='background:#1E2A3E; padding:28px 40px; text-align:center;'>"
                + "<span style='font-size:22px; font-weight:700; color:#fff; letter-spacing:-0.5px;'>아파트엔</span>"
                + "<span style='font-size:12px; color:#7B8EA8; margin-left:8px;'>APTeN</span>"
                + "</div>"

                // 본문
                + "<div style='padding:40px;'>"
                + "<p style='font-size:13px; color:#A0AEC0; margin:0 0 8px;'>" + java.time.LocalDate.now() + "</p>"
                + "<h2 style='font-size:20px; font-weight:700; color:#1A202C; margin:0 0 16px;'>"
                + "<strong>비밀번호 재설정</strong> 안내"
                + "</h2>"
                + "<p style='font-size:14px; color:#4A5568; line-height:1.7; margin:0 0 24px;'>"
                + "아래 버튼을 클릭하여 비밀번호를 재설정하세요.<br>"
                + "링크는 <strong>30분</strong> 후 만료됩니다."
                + "</p>"

                // 버튼
                + "<div style='text-align:center; margin: 32px 0;'>"
                + "<a href='" + resetUrl + "' "
                + "style='display:inline-block; padding:14px 32px; background:#1E2A3E; color:#fff; "
                + "text-decoration:none; border-radius:8px; font-size:15px; font-weight:600;'>"
                + "비밀번호 재설정하기"
                + "</a>"
                + "</div>"

                + "<p style='font-size:12px; color:#A0AEC0; margin:24px 0 0; line-height:1.6;'>"
                + "본인이 요청하지 않은 경우 이 메일을 무시하세요.<br>"
                + "링크는 30분 후 자동으로 만료됩니다."
                + "</p>"
                + "</div>"

                // 푸터
                + "<div style='background:#f5f6fa; padding:20px 40px; text-align:center; border-top:1px solid #E2E8F0;'>"
                + "<p style='font-size:12px; color:#A0AEC0; margin:0;'>© 2026 APTeN 아파트엔. All rights reserved.</p>"
                + "</div>"

                + "</div>"
                + "</div>"
                + "</body></html>";
    }
}