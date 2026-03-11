package com.apt.mail.service;

import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import com.apt.mail.dto.request.ResetPasswordReq;
import com.apt.mail.mapper.PasswordResetTokenMapper;
import com.apt.mail.model.PasswordResetToken;
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
        return "<div style='font-family: sans-serif; max-width: 600px; margin: 0 auto;'>"
                + "<h2 style='color: #1E2533;'>비밀번호 재설정</h2>"
                + "<p>아래 버튼을 클릭하여 비밀번호를 재설정하세요.</p>"
                + "<p>링크는 <strong>30분</strong> 후 만료됩니다.</p>"
                + "<a href='" + resetUrl + "' "
                + "style='display: inline-block; padding: 12px 24px; "
                + "background: #1E2533; color: #fff; text-decoration: none; "
                + "border-radius: 8px; margin-top: 16px;'>비밀번호 재설정</a>"
                + "</div>";
    }
}