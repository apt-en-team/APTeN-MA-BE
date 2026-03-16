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
    private String authorName;
    private String authorUnit;
}
