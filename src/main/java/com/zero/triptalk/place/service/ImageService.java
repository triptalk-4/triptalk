package com.zero.triptalk.place.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zero.triptalk.exception.code.ImageUploadErrorCode;
import com.zero.triptalk.exception.type.ImageException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client amazonS3Client;

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
                amazonS3Client.putObject(putObjectRequest);

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
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public void deleteImages(List<String> imageUrls) {
        List<String> objectKeys = imageUrls.stream().map(this::convertUrlToObjectKey).collect(Collectors.toList());

        try {
            DeleteObjectsRequest dor = new DeleteObjectsRequest(bucket)
                    .withKeys(objectKeys.toArray(new String[0]));
            amazonS3Client.deleteObjects(dor);
        } catch (AmazonServiceException e) {
            throw new ImageException(ImageUploadErrorCode.IMAGE_DELETE_FAILED);
        }
    }

    private String convertUrlToObjectKey(String url) {
        return url.substring(url.indexOf(bucket) + bucket.length() + 1);
    }

    public void deleteImage(String imageUrl) {
        deleteImages(Collections.singletonList(imageUrl));
    }

}