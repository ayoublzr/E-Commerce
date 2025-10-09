package com.lazaar.ecommerce.Security;

import com.lazaar.ecommerce.DTO.request.RefreshTokenRequest;
import com.lazaar.ecommerce.DTO.request.ResetPasswordRequest;
import com.lazaar.ecommerce.DTO.request.SignUpRequest;
import com.lazaar.ecommerce.DTO.request.SigninRequest;
import com.lazaar.ecommerce.DTO.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse SignUp(SignUpRequest request);
    JwtAuthenticationResponse SignIn(SigninRequest request);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest request);

    void sendForgotPasswordEmail(String email);
    void verifyOTP(String email, String otp);
    void resetPassword(ResetPasswordRequest request);
}
