package com.apt.infra.mail.controller;

import com.apt.common.response.ResultResponse;
import com.apt.infra.mail.dto.request.PasswordResetReq;
import com.apt.infra.mail.dto.request.ResetPasswordReq;
import com.apt.infra.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 비밀번호 재설정 관련 API
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    // 비밀번호 재설정 이메일 발송 요청
    // POST /api/mail/password-reset
    @PostMapping("/password-reset")
    public ResultResponse<Void> sendPasswordResetEmail(@RequestBody PasswordResetReq req) {
        mailService.sendPasswordResetEmail(req.getEmail());
        return new ResultResponse<>("비밀번호 재설정 메일이 발송되었습니다.", null);
    }

    // 비밀번호 재설정 처리
    // POST /api/mail/reset-password
    @PostMapping("/reset-password")
    public ResultResponse<Void> resetPassword(@RequestBody ResetPasswordReq req) {
        mailService.resetPassword(req);
        return new ResultResponse<>("비밀번호가 성공적으로 변경되었습니다.", null);
    }
}