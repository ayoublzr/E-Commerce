package com.lazaar.ecommerce.DTO;

import com.lazaar.ecommerce.DTO.response.DtoUser;
import com.lazaar.ecommerce.models.User;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserMapper implements Function<User, DtoUser> {

    @Override
    public DtoUser apply(User user) {
        return new DtoUser(
                user.getId(), user.getFullName(), user.getEmail(), user.getRole()
        );
    }
}
