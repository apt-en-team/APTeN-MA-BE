package com.apt.board.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardPageRes<T> {
    private List<T> content;
    private int maxPage;
    private int totalCount;

    public static <T> BoardPageRes<T> of(List<T> content, int maxPage, int totalCount) {
        BoardPageRes<T> res = new BoardPageRes<>();
        res.content = content;
        res.maxPage = maxPage;
        res.totalCount = totalCount;
        return res;
    }
}
