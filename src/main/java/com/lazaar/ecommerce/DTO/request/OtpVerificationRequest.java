package com.lazaar.ecommerce.DTO.request;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
