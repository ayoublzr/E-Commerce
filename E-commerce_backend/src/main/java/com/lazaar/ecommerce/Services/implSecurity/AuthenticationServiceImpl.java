package com.lazaar.ecommerce.Services.implSecurity;

import com.lazaar.ecommerce.DTO.request.RefreshTokenRequest;
import com.lazaar.ecommerce.DTO.request.ResetPasswordRequest;
import com.lazaar.ecommerce.DTO.request.SignUpRequest;
import com.lazaar.ecommerce.DTO.request.SigninRequest;
import com.lazaar.ecommerce.DTO.response.JwtAuthenticationResponse;
import com.lazaar.ecommerce.Exception.OTPExpiredException;
import com.lazaar.ecommerce.Exception.TokenRefreshException;
import com.lazaar.ecommerce.Repositories.UserRepository;
import com.lazaar.ecommerce.Security.AuthenticationService;
import com.lazaar.ecommerce.Security.JwtService;
import com.lazaar.ecommerce.Security.RefreshTokenService;
import com.lazaar.ecommerce.models.RefreshToken;
import com.lazaar.ecommerce.models.Role;
import com.lazaar.ecommerce.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;

    @Autowired
    private final EmailService emailService;


    @Override
    public JwtAuthenticationResponse SignUp(SignUpRequest request) {

if (userRepository.findByEmail(request.getEmail()).isPresent()){
    throw new RuntimeException("Email already in use");
}
        Role role = request.getRole() != null ? request.getRole() : Role.CUSTOMER;
        var user = User.builder()
                .firstName(request.getFirstName())
                .LastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        emailVerificationService.sendVerificationEmail(user);
        return JwtAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .role(user.getRole().name())
                .tokenType("Bearer")
                .isVerified(false)
                .build();
    }

    @Override
    public JwtAuthenticationResponse SignIn(SigninRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if(!user.isVerified()){
            throw new RuntimeException("User is not verified " + "Please check your email for verification link");
        }

        var jwtToken = jwtService.generateToken(user);
         var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return JwtAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .role(user.getRole().name())
                .tokenType("Bearer")
                .isVerified(user.isVerified())
                .build();
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return JwtAuthenticationResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .userId(user.getId())
                            .role(user.getRole().name())
                            .tokenType("Bearer")
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public void sendForgotPasswordEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OTPExpiredException("User with email " + email + " not found"));

        String otp = generateOTP();
        user.setOtp(otp);
        // 🔑 Set OTP expiry time to 5 minutes from now
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), otp);
    }

    private String generateOTP() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void verifyOTP(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OTPExpiredException("User with email " + email + " not found"));

        if (!user.getOtp().equals(otp)) {
            throw new OTPExpiredException("Invalid OTP");
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OTPExpiredException("OTP has expired");
        }
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new OTPExpiredException("User with email " + request.getEmail() + " not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


}
