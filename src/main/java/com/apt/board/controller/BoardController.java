package com.apt.board.controller;

import com.apt.board.dto.request.BoardReq;
import com.apt.board.dto.response.BoardRes;
import com.apt.board.dto.response.BoardPageRes;
import com.apt.board.service.BoardService;
import com.apt.board.service.FileService;
import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping
    public ResultResponse<BoardPageRes<BoardRes>> getPostList(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        return ResultResponse.success("게시글 목록 조회 성공", boardService.getPostList(category, page, size, keyword));
    }

    @GetMapping("/popular")
    public ResultResponse<List<BoardRes>> getPopularPosts() {
        return ResultResponse.success("인기글 조회 성공", boardService.getPopularPosts());
    }

    @GetMapping("/my")
    public ResultResponse<List<BoardRes>> getMyPosts(
            @RequestParam(required = false) Integer size,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResultResponse.success("내 글 조회 성공", boardService.getMyPosts(userPrincipal.getUserId(), size));
    }

    @PostMapping("/image")
    public ResultResponse<Map<String, String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String imageUrl = fileService.saveTempImage(file, userPrincipal.getUserId());
        log.info("임시 이미지 업로드 완료 - userId: {}, url: {}", userPrincipal.getUserId(), imageUrl);
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

    @PostMapping
    public ResultResponse<BoardRes> createPost(
            @RequestBody BoardReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResultResponse.success("게시글 등록 성공", boardService.createPost(userPrincipal.getUserId(), req));
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
        boardService.deletePost(userPrincipal.getUserId(), id);
        return ResultResponse.success("게시글 삭제 성공", null);
    }

    @GetMapping("/{id}")
    public ResultResponse<BoardRes> getPostDetail(@PathVariable Long id) {
        return ResultResponse.success("게시글 상세 조회 성공", boardService.getPostDetail(id));
    }
}