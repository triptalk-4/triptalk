package com.zero.triptalk.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zero.triptalk.exception.code.ImageUploadErrorCode;
import com.zero.triptalk.exception.custom.ImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //사진 저장
    public List<String> uploadFiles(List<MultipartFile> multipartFiles) {
        ArrayList<String> urlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = generateFileName(file.getOriginalFilename());
            try {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                InputStream inputStream = file.getInputStream();
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3.putObject(putObjectRequest);

                String fileUrl = generateS3FileUrl(fileName);
                urlList.add(fileUrl);
                inputStream.close();
            } catch (IOException e) {
                throw new ImageException(ImageUploadErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        return urlList;
    }

    private String generateFileName(String originalFileName) {
        String ext = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + ext;
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ImageException(ImageUploadErrorCode.BAD_REQUEST);
        }
    }

    private String generateS3FileUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    //사진 리스트 삭제
    public void deleteFiles(List<String> imageUrls) {
        List<String> objectKeys = imageUrls.stream().map(this::convertUrlToObjectKey).collect(Collectors.toList());

        for (String x : objectKeys) {
            try {
                DeleteObjectRequest request = new DeleteObjectRequest(bucket, x);
                amazonS3.deleteObject(request);
            } catch (AmazonServiceException e) {
                log.error(e.getErrorMessage());
                throw new ImageException(ImageUploadErrorCode.IMAGE_DELETE_FAILED);
            }
        }
    }

    private String convertUrlToObjectKey(String url) {
        return url.substring(url.indexOf(".com/") + 5);
    }

    //S3에서 사진 삭제
    public void deleteFile(String imageUrl) {
        deleteFiles(Collections.singletonList(imageUrl));
    }

}