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
                               @Param("size") int size,
                               @Param("keyword") String keyword);

    List<BoardRes> getAdminPostList(@Param("category") String category,
                                    @Param("offset") int offset,
                                    @Param("size") int size,
                                    @Param("isDeleted") Integer isDeleted);

    int getTotalCountAdmin(@Param("category") String category,
                           @Param("isDeleted") Integer isDeleted);

    BoardRes getAdminPostDetail(Long id);

    int getTotalCount(@Param("category") String category,
                      @Param("keyword") String keyword);

    BoardRes getPostDetail(Long id);

    void increaseViewCount(Long id);

    void createPost(@Param("userId") long userId,
                    @Param("req") BoardReq req);

    void updateContent(@Param("boardId") long boardId,
                       @Param("content") String content,
                       @Param("imageUrl") String imageUrl);

    void updatePost(@Param("userId") long userId,
                    @Param("req") BoardReq req);

    void deletePost(Long boardId);

    List<BoardRes> getPopularPosts();

    List<BoardRes> getMyPosts(@Param("userId") Long userId, @Param("size") Integer size);

    int countByCategory(@Param("category") String category);
    int countToday();
    int countDeleted();
    int countAllComments();
}