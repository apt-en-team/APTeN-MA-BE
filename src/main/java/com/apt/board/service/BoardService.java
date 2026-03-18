package com.apt.board.service;

import com.apt.board.dto.request.BoardReq;
import com.apt.board.dto.response.BoardRes;
import com.apt.board.dto.response.BoardPageRes;
import com.apt.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    // 게시글 목록 조회
    public BoardPageRes<BoardRes> getPostList(String category, int page, int size) {
        int offset = (page - 1) * size;
        List<BoardRes> list = boardMapper.getPostList(category, offset, size);
        int totalCount = boardMapper.getTotalCount(category);
        int maxPage = (int) Math.ceil((double) totalCount / size);
        return BoardPageRes.of(list, maxPage, totalCount);
    }

    // 게시글 상세
    public BoardRes getPostDetail(Long id) {
        boardMapper.increaseViewCount(id);
        return boardMapper.getPostDetail(id);
    }

    // 게시글 등록
    public BoardRes createPost(long userId, BoardReq request) {
        boardMapper.createPost(userId, request);
        return boardMapper.getPostDetail(request.getBoardId());
    }
}