package com.apt.board.mapper;

import com.apt.board.dto.request.BoardReq;
import com.apt.board.dto.response.BoardRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    List<BoardRes> getPostList(@Param("category") String category,
                               @Param("offset") int offset,
                               @Param("size") int size);

    int getTotalCount(String category);

    BoardRes getPostDetail(Long id);

    void increaseViewCount(Long id);

    void createPost(@Param("userId") long userId,
                    @Param("req") BoardReq req);
}