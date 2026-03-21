package com.apt.board.mapper;

import com.apt.board.dto.request.CommentReq;
import com.apt.board.dto.response.CommentRes;
import com.apt.board.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 댓글 목록 조회 (삭제된 댓글 포함 - 삭제 표시용)
    List<CommentRes> findByBoardId(Long boardId);

    // 댓글 단건 조회 (수정/삭제 권한 체크용)
    CommentRes findById(Long commentId);

    // 댓글 등록
    void insert(Comment comment);

    // 댓글 수정
    void update(@Param("commentId") Long commentId,
                @Param("req") CommentReq req);

    // 댓글 삭제 (soft delete)
    void delete(Long commentId);

    // 게시글 삭제 시 댓글도 삭제
    void softDeleteByBoardId(Long boardId);
}