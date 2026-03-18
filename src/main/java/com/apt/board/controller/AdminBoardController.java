package com.apt.board.controller;

import com.apt.board.dto.request.BoardReq;
import com.apt.board.dto.response.BoardPageRes;
import com.apt.board.dto.response.BoardRes;
import com.apt.board.service.BoardService;
import com.apt.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("api/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController {
    private final BoardService boardService;

    /* 게시글 목록 조회 */
    @GetMapping
    public ResponseEntity<BoardPageRes<BoardRes>> getPostList(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(boardService.getPostList(category, page, size));
    }

    /* 게시글 상세 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<BoardRes> getPostDetail(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getPostDetail(id));
    }

    /* 게시글 등록 */
    @PostMapping
    public ResponseEntity<BoardRes> createPost(
            @RequestBody BoardReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        return ResponseEntity.ok(boardService.createPost(userPrincipal.getUserId(), req));
    }
}
