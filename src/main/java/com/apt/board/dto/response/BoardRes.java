package com.apt.board.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardRes {
    private Long boardId;
    private Long userId;
    private String category;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 추가
    private String authorName;   // user.name
    private int commentCount;    // comment 테이블 COUNT
}
