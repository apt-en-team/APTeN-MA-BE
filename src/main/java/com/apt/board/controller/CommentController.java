package com.apt.board.controller;

import com.apt.board.dto.request.CommentReq;
import com.apt.board.dto.response.CommentRes;
import com.apt.board.service.CommentService;
import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 조회
    @GetMapping("/api/boards/{boardId}/comments")
    public ResultResponse<List<CommentRes>> getComments(@PathVariable Long boardId) {
        return ResultResponse.success("댓글 목록 조회 성공", commentService.getComments(boardId));
    }

    // 댓글 등록
    @PostMapping("/api/boards/{boardId}/comments")
    public ResultResponse<CommentRes> createComment(
            @PathVariable Long boardId,
            @RequestBody @Valid CommentReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResultResponse.success("댓글 등록 성공",
                commentService.createComment(boardId, userPrincipal.getUserId(), req));
    }

    // 댓글 수정
    @PutMapping("/api/comments/{commentId}")
    public ResultResponse<CommentRes> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResultResponse.success("댓글 수정 성공",
                commentService.updateComment(commentId, userPrincipal.getUserId(), userPrincipal.getRole(), req));
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public ResultResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        commentService.deleteComment(commentId, userPrincipal.getUserId(), userPrincipal.getRole());
        return ResultResponse.success("댓글 삭제 성공", null);
    }
}