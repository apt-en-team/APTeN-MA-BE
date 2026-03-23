package com.apt.board.controller;

import com.apt.board.dto.request.BoardReq;
import com.apt.board.dto.response.BoardPageRes;
import com.apt.board.dto.response.BoardRes;
import com.apt.board.dto.response.BoardStatsRes;
import com.apt.board.service.BoardService;
import com.apt.board.service.FileService;
import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping
    public ResultResponse<BoardPageRes<BoardRes>> getPostList(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) Integer isDeleted
    ) {
        return ResultResponse.success("게시글 목록 조회 성공", boardService.getAdminPostList(category, page, size, isDeleted));
    }

    @GetMapping("/stats")
    public ResultResponse<BoardStatsRes> getStats() {
        return ResultResponse.success("통계 조회 성공", boardService.getAdminBoardStats());
    }

    @PostMapping
    public ResultResponse<BoardRes> createPost(
            @RequestBody BoardReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResultResponse.success("게시글 등록 성공", boardService.createPost(userPrincipal.getUserId(), req));
    }

    @PostMapping("/image")
    public ResultResponse<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String imageUrl = fileService.saveTempImage(file, userPrincipal.getUserId());
        log.info("관리자 이미지 업로드 완료 - userId: {}, url: {}", userPrincipal.getUserId(), imageUrl);
        return ResultResponse.success("이미지 업로드 성공", Map.of("imageUrl", imageUrl));
    }

    @PostMapping("/file")
    public ResultResponse<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String fileUrl = fileService.saveTempFile(file, userPrincipal.getUserId());
        String originalName = file.getOriginalFilename();
        return ResultResponse.success("파일 업로드 성공", Map.of("fileUrl", fileUrl, "fileName", originalName));
    }

    @GetMapping("/{id}")
    public ResultResponse<BoardRes> getPostDetail(@PathVariable Long id) {
        return ResultResponse.success("게시글 상세 조회 성공", boardService.getAdminPostDetail(id));
    }

    @PutMapping("/{id}")
    public ResultResponse<BoardRes> updatePost(
            @PathVariable Long id,
            @RequestBody BoardReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResultResponse.success("게시글 수정 성공", boardService.updatePost(userPrincipal.getUserId(), id, req));
    }

    @DeleteMapping("/{id}")
    public ResultResponse<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        boardService.deletePostByAdmin(id);
        return ResultResponse.success("게시글 삭제 성공", null);
    }
}