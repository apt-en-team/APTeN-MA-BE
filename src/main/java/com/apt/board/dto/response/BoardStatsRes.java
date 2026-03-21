package com.apt.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardStatsRes {
    private int totalCount;
    private int noticeCount;
    private int freeCount;
    private int inquiryCount;
    private int todayCount;
    private int deletedCount;
    private int commentCount;
}
