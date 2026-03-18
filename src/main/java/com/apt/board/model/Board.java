package com.apt.board.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Board {
    private Long boardId;
    private Long userId;
    private String category;      // NOTICE, FREE, INQUIRY
    private String title;
    private String content;
    private String imageUrl;      // 이미지 URL (없으면 null)
    private int viewCount;
    private int isDeleted;        // 0: 정상, 1: 삭제
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}