package com.apt.board.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardReq {
    private Long boardId;   // INSERT 후 MyBatis가 생성된 PK를 여기에 넣어줌
    private String category;
    private String title;
    private String content;
}