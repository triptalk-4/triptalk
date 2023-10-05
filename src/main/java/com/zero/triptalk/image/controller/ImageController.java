package com.zero.triptalk.image.controller;


import com.zero.triptalk.image.domain.ImageRequest;
import com.zero.triptalk.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    //사진 저장
    @PostMapping("/images")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<String>> uploadImages(@RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(imageService.uploadFiles(files));
    }

    //S3에서 사진 삭제
    @DeleteMapping("/images")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deleteImage(@RequestBody ImageRequest request){
        imageService.deleteImage(request.getUrl());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
