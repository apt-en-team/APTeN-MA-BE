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
    private String imageUrl;     // 이미지 URL (없으면 null → 프론트에서 thumbnail 없는 카드 처리)
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 추가
    private String authorName;   // user.name
    private String authorUnit; // household.dong + ho  ← 추가
    private int commentCount;    // comment 테이블 COUNT
    // private int likeCount;    // 좋아요 기능 없으면 생략
    // private String thumbnail; // 썸네일 기능 없으면 생략
}
