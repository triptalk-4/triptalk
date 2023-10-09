package com.zero.triptalk.user.response;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailCheckResponse {
   private String PostMailOk;

   private String emailToken;

   public EmailCheckResponse(String message) {
   }
}
