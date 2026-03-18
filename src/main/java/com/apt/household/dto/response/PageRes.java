package com.apt.household.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRes {
    private int maxPage;
    private int totalCount;

    public PageRes(int maxPage, int totalCount){
        this.maxPage = maxPage;
        this.totalCount = totalCount;
    }
}
