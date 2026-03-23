package com.apt.board.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRes {
    private Long commentId;
    private Long boardId;
    private Long userId;
    private String content;
    private int isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorName;  // 작성자 이름
    private String authorUnit;  // 작성자 세대 (동/호)
    private String role;        // 작성자 역할 (ADMIN / RESIDENT)
}