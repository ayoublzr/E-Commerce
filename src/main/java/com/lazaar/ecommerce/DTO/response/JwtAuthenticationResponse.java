package com.lazaar.ecommerce.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;  // Changed to lowercase 'a' to match standard conventions
    private Long userId;
    private String role;
    private String refreshToken;
    private String tokenType;
    private boolean isVerified;

    // Explicit getter with uppercase 'A'
    public String getAccessToken() {
        return this.accessToken;
    }

}