package com.apt.board.service;

import com.apt.board.dto.request.BoardReq;
import com.apt.board.dto.response.BoardRes;
import com.apt.board.dto.response.BoardPageRes;
import com.apt.board.dto.response.BoardStatsRes;
import com.apt.board.mapper.BoardImageMapper;
import com.apt.board.mapper.BoardMapper;
import com.apt.board.mapper.CommentMapper;
import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final BoardImageMapper boardImageMapper;
    private final FileService fileService;
    private final CommentMapper commentMapper;

    // 게시글 목록 조회 (입주자)
    public BoardPageRes<BoardRes> getPostList(String category, int page, int size, String keyword) {
        int offset = (page - 1) * size;
        List<BoardRes> list = boardMapper.getPostList(category, offset, size, keyword);
        int totalCount = boardMapper.getTotalCount(category, keyword);
        int maxPage = (int) Math.ceil((double) totalCount / size);
        return BoardPageRes.of(list, maxPage, totalCount);
    }

    // 게시글 목록 조회 (관리자)
    public BoardPageRes<BoardRes> getAdminPostList(String category, int page, int size, Integer isDeleted) {
        int offset = (page - 1) * size;
        List<BoardRes> list = boardMapper.getAdminPostList(category, offset, size, isDeleted);
        int totalCount = boardMapper.getTotalCountAdmin(category, isDeleted);
        int maxPage = (int) Math.ceil((double) totalCount / size);
        return BoardPageRes.of(list, maxPage, totalCount);
    }

    // 입주자 게시글 상세
    public BoardRes getPostDetail(Long id) {
        boardMapper.increaseViewCount(id);
        return boardMapper.getPostDetail(id);
    }

    // 관리자 게시글 상세
    public BoardRes getAdminPostDetail(Long id) {
        return boardMapper.getAdminPostDetail(id);
    }

    // 게시글 등록
    @Transactional
    public BoardRes createPost(long userId, BoardReq req) {
        boardMapper.createPost(userId, req);
        long boardId = req.getBoardId();

        List<String> tempUrls = extractImageUrls(req.getContent());
        if (!tempUrls.isEmpty()) {
            List<String> realUrls = new ArrayList<>();
            StringBuilder newContent = new StringBuilder(req.getContent());

            for (String tempUrl : tempUrls) {
                String realUrl = fileService.moveTempToBoard(tempUrl, userId, boardId);
                realUrls.add(realUrl);
                int idx = newContent.indexOf(tempUrl);
                if (idx != -1) newContent.replace(idx, idx + tempUrl.length(), realUrl);
            }

            req.setContent(newContent.toString());
            req.setImageUrl(realUrls.get(0));
            boardMapper.updateContent(boardId, req.getContent(), req.getImageUrl());
            boardImageMapper.insertImages(boardId, realUrls);
        }

        return boardMapper.getPostDetail(boardId);
    }

    // 게시글 수정
    @Transactional
    public BoardRes updatePost(long userId, Long boardId, BoardReq req) {
        BoardRes existing = boardMapper.getPostDetail(boardId);
        if (existing == null) throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        if (!existing.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);

        List<String> tempUrls = extractImageUrls(req.getContent());
        if (!tempUrls.isEmpty()) {
            List<String> realUrls = new ArrayList<>();
            StringBuilder newContent = new StringBuilder(req.getContent());

            for (String url : tempUrls) {
                String realUrl;
                if (url.contains("/temp/")) {
                    // 새로 업로드한 이미지 → temp에서 실제 경로로 이동
                    realUrl = fileService.moveTempToBoard(url, userId, boardId);
                } else {
                    // 기존 이미지 → 그대로 사용
                    realUrl = url;
                }
                realUrls.add(realUrl);
                int idx = newContent.indexOf(url);
                if (idx != -1) newContent.replace(idx, idx + url.length(), realUrl);
            }

            req.setContent(newContent.toString());
            req.setImageUrl(realUrls.get(0));
            boardImageMapper.deleteByBoardId(boardId);
            boardImageMapper.insertImages(boardId, realUrls);
        } else {
            req.setImageUrl(null);
            boardImageMapper.deleteByBoardId(boardId);
        }

        req.setBoardId(boardId);
        boardMapper.updatePost(userId, req);
        return boardMapper.getPostDetail(boardId);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(long userId, Long boardId) {
        BoardRes existing = boardMapper.getPostDetail(boardId);
        if (existing == null) throw new CustomException(ErrorCode.BOARD_NOT_FOUND);
        if (!existing.getUserId().equals(userId)) throw new CustomException(ErrorCode.FORBIDDEN);

        commentMapper.softDeleteByBoardId(boardId);
        boardImageMapper.deleteByBoardId(boardId);
        fileService.deleteBoardFolder(userId, boardId);
        boardMapper.deletePost(boardId);
    }

    // 인기글 조회
    public List<BoardRes> getPopularPosts() {
        return boardMapper.getPopularPosts();
    }

    // 내가 쓴 글 조회
    public List<BoardRes> getMyPosts(Long userId, Integer size) {
        return boardMapper.getMyPosts(userId, size);
    }

    // content에서 이미지 URL 전체 추출
    private List<String> extractImageUrls(String content) {
        List<String> urls = new ArrayList<>();
        if (content == null || content.isEmpty()) return urls;
        Pattern pattern = Pattern.compile("<img[^>]+src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) urls.add(matcher.group(1));
        return urls;
    }

    // 관리자 게시글 통계 서비스
    public BoardStatsRes getAdminBoardStats() {
        return BoardStatsRes.builder()
                .totalCount(boardMapper.countByCategory(""))
                .noticeCount(boardMapper.countByCategory("NOTICE"))
                .freeCount(boardMapper.countByCategory("FREE"))
                .inquiryCount(boardMapper.countByCategory("INQUIRY"))
                .todayCount(boardMapper.countToday())
                .deletedCount(boardMapper.countDeleted())
                .commentCount(boardMapper.countAllComments())
                .build();
    }

    // 관리자 게시글 삭제 (권한 체크 없음)
    @Transactional
    public void deletePostByAdmin(Long boardId) {
        BoardRes existing = boardMapper.getPostDetail(boardId);
        if (existing == null) throw new CustomException(ErrorCode.BOARD_NOT_FOUND);

        commentMapper.softDeleteByBoardId(boardId);
        boardImageMapper.deleteByBoardId(boardId);
        fileService.deleteBoardFolder(existing.getUserId(), boardId);
        boardMapper.deletePost(boardId);
    }
}