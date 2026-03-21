package com.apt.board.mapper;

import com.apt.board.model.BoardImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardImageMapper {
    // 이미지 목록 저장 (글 등록/수정 시)
    void insertImages(@Param("boardId") Long boardId,
                      @Param("images") List<String> imageUrls);

    // 이미지 목록 조회
    List<BoardImage> findByBoardId(Long boardId);

    // 이미지 전체 삭제 (글 삭제 시)
    void deleteByBoardId(Long boardId);
}
