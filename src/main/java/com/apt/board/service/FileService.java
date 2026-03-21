package com.apt.board.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.apt.common.exception.CustomException;
import com.apt.common.exception.ErrorCode;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${file.directory}")
    private String directory;

    /*
     * 임시 폴더에 이미지 저장
     * 경로: {directory}/{userId}/temp/uuid.jpg
     * URL:  /apten/uploads/{userId}/temp/uuid.jpg
     */
    public String saveTempImage(MultipartFile file, long userId) {
        validateFile(file);
        try {
            Path tempPath = Paths.get(directory, String.valueOf(userId), "temp");
            Files.createDirectories(tempPath);

            String extension = getExtension(file.getOriginalFilename());
            if (extension.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_FILE_PATH);
            }
            String originalName = file.getOriginalFilename();
            String savedFilename = UUID.randomUUID() + "_" + originalName;
            Path targetPath = tempPath.resolve(savedFilename);
            file.transferTo(targetPath.toFile());
            log.info("임시 파일 저장 완료: {}", targetPath.toAbsolutePath());

            return "/apten/uploads/" + userId + "/temp/" + savedFilename;
        } catch (IOException e) {
            log.error("임시 파일 저장 실패", e);
            throw new CustomException(ErrorCode.FILE_SAVE_FAILED);
        }
    }

    /*
     * temp 폴더의 이미지를 실제 경로로 이동
     * temp:  {directory}/{userId}/temp/uuid.jpg
     * 실제:  {directory}/{userId}/{boardId}/uuid.jpg
     */
    public String moveTempToBoard(String tempUrl, long userId, long boardId) {
        try {
            if (tempUrl == null || !tempUrl.contains("/temp/")) {
                throw new CustomException(ErrorCode.INVALID_FILE_PATH);
            }

            // URL → 실제 Path 변환
            Path tempFilePath = resolvePathFromUrl(tempUrl);

            // userId 검증 (OS 독립 방식)
            // 경로 정규화 (Path Traversal 방지)
            Path userPath = Paths.get(directory, String.valueOf(userId)).toAbsolutePath().normalize();
            Path normalized = tempFilePath.toAbsolutePath().normalize();

            if (!normalized.startsWith(userPath)) {
                throw new CustomException(ErrorCode.FILE_ACCESS_DENIED);
            }

            // temp에 없으면 이미 이동된 파일 (수정 시 기존 이미지) → 그대로 반환
            if (!Files.exists(tempFilePath)) {
                return tempUrl;
            }

            // 실제 경로로 이동
            Path boardPath = Paths.get(directory, String.valueOf(userId), String.valueOf(boardId));
            Files.createDirectories(boardPath);

            String filename = tempFilePath.getFileName().toString();
            Path destPath = boardPath.resolve(filename);
            Files.move(tempFilePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("파일 이동 완료: {} → {}", tempFilePath, destPath);

            return "/apten/uploads/" + userId + "/" + boardId + "/" + filename;
        } catch (IOException e) {
            log.error("파일 이동 실패", e);
            throw new CustomException(ErrorCode.FILE_MOVE_FAILED);
        }
    }

    // 폴더에 문서 파일 저장
    public String saveTempFile(MultipartFile file, long userId) {
        validateDocFile(file);
        try {
            Path tempPath = Paths.get(directory, String.valueOf(userId), "temp");
            Files.createDirectories(tempPath);

            String originalName = file.getOriginalFilename();
            String savedFilename = UUID.randomUUID() + "_" + originalName;
            log.info("저장 파일명: {}", savedFilename);
            Path targetPath = tempPath.resolve(savedFilename);
            file.transferTo(targetPath.toFile());

            return "/apten/uploads/" + userId + "/temp/" + savedFilename;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_SAVE_FAILED);
        }
    }

    private void validateDocFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new CustomException(ErrorCode.FILE_EMPTY);
        if (file.getSize() > 20 * 1024 * 1024)
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
        String extension = getExtension(file.getOriginalFilename()).toLowerCase();
        if (!extension.matches("pdf|doc|docx|xls|xlsx|hwp|hwpx|ppt|pptx"))
            throw new CustomException(ErrorCode.FILE_TYPE_NOT_ALLOWED);
    }

    /*
     * 게시글 폴더 전체 삭제
     * 경로: {directory}/{userId}/{boardId}/
     */
    public void deleteBoardFolder(long userId, long boardId) {
        Path boardPath = Paths.get(directory, String.valueOf(userId), String.valueOf(boardId));
        if (!Files.exists(boardPath)) return;
        try {
            Files.walkFileTree(boardPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            log.info("게시글 폴더 삭제 완료: {}", boardPath.toAbsolutePath());
        } catch (IOException e) {
            log.error("폴더 삭제 실패", e);
            throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
        }
    }

    /*
     * URL → 실제 파일 Path 변환
     * /apten/uploads/{userId}/temp/{filename}
     *   → {directory}/{userId}/temp/{filename}
     *
     * URL 앞의 /apten/uploads/ prefix를 제거하고
     * directory 기준 실제 경로로 조합
     */
    private Path resolvePathFromUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            int idx = url.indexOf("/apten/uploads/");
            if (idx == -1) throw new CustomException(ErrorCode.INVALID_FILE_PATH);
            url = url.substring(idx); // /apten/uploads/... 만 추출
        }

        String prefix = "/apten/uploads/";
        if (!url.startsWith(prefix)) {
            throw new CustomException(ErrorCode.INVALID_FILE_PATH);
        }
        String relativePath = url.substring(prefix.length());
        if (relativePath.contains("..")) {
            throw new CustomException(ErrorCode.FILE_ACCESS_DENIED);
        }
        return Paths.get(directory, relativePath);
    }

    // ── 유효성 검사 ───────────────────────────────────────────────
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new CustomException(ErrorCode.FILE_EMPTY);
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/"))
            throw new CustomException(ErrorCode.IMAGE_TYPE_NOT_ALLOWED);
        String extension = getExtension(file.getOriginalFilename()).toLowerCase();
        if (!extension.matches("jpg|jpeg|png|gif|webp"))
            throw new CustomException(ErrorCode.FILE_TYPE_NOT_ALLOWED);
        if (file.getSize() > 10 * 1024 * 1024)
            throw new CustomException(ErrorCode.IMAGE_SIZE_EXCEEDED);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}