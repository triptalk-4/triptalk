package com.zero.triptalk.user.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRegisterRequest {
    private String email;
    private String oldPassword;
    private String newPasswordCheck;
    private String newPassword;
    private String newNickname;
    private String newAboutMe;



}
