package com.lazaar.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(
                name = "uc_user_email",
                columnNames = "email"
        )
)

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private String firstName;
    @Getter @Setter
    @Column
    private String LastName;
    @Column(name = "fullname")
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Role role;

    @Column
    private String otp;

    @Column
    private LocalDateTime otpExpiry;

    // ================ Email Verification ================
    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;
// false by default → becomes true after user verifies via email link

    @Column(name = "verification_token")
    private String verificationToken;
// Random token (UUID or secure string) sent via email for verification

    @Column(name = "verification_token_expiry")
    private LocalDateTime verificationTokenExpiry;
// Expiration time for the verification token (e.g., 24 hours after signup)


    // ================ Account Status ================
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;
// If false → account disabled (e.g., by admin or after too many failed logins)


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }



    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
