package com.apt.board.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Comment {
    private Long commentId;
    private Long boardId;
    private Long userId;
    private String content;
    private int isDeleted;        // 0: 정상, 1: 삭제
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}