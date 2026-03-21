package com.apt.board.service;

import com.apt.board.dto.request.CommentReq;
import com.apt.board.dto.response.CommentRes;
import com.apt.board.mapper.CommentMapper;
import com.apt.board.model.Comment;
import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    // 댓글 목록 조회
    public List<CommentRes> getComments(Long boardId) {
        return commentMapper.findByBoardId(boardId);
    }

    // 댓글 등록
    @Transactional
    public CommentRes createComment(Long boardId, long userId, CommentReq req) {
        Comment comment = new Comment();
        comment.setBoardId(boardId);
        comment.setUserId(userId);
        comment.setContent(req.getContent());
        commentMapper.insert(comment);
        return commentMapper.findById(comment.getCommentId()); // 생성된 키 바로 사용
    }

    // 댓글 수정
    @Transactional
    public CommentRes updateComment(Long commentId, long userId, String role, CommentReq req) {
        CommentRes comment = findCommentOrThrow(commentId);
        checkPermission(comment, userId, role);
        commentMapper.update(commentId, req);
        return commentMapper.findById(commentId);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, long userId, String role) {
        CommentRes comment = findCommentOrThrow(commentId);
        checkPermission(comment, userId, role);
        commentMapper.delete(commentId);
    }

    // ── private ──────────────────────────────────────────────

    private CommentRes findCommentOrThrow(Long commentId) {
        CommentRes comment = commentMapper.findById(commentId);
        if (comment == null || comment.getIsDeleted() == 1) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        return comment;
    }

    // 수정/삭제 권한 체크: 본인 or 관리자
    private void checkPermission(CommentRes comment, long userId, String role) {
        boolean isAdmin = "ADMIN".equals(role);
        boolean isOwner = comment.getUserId().equals(userId);
        if (!isAdmin && !isOwner) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}