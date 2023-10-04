package com.zero.triptalk.auth.client;

import com.zero.triptalk.auth.dto.GoogleRequestToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "googleAuth", url="https://oauth2.googleapis.com")
public interface FeignClientGoogleAuth {
    @PostMapping("/token")
    ResponseEntity<String> getAccessToken(@RequestBody GoogleRequestToken request);
}
