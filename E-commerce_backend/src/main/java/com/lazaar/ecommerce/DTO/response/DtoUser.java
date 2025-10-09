package com.lazaar.ecommerce.DTO.response;

import com.lazaar.ecommerce.models.Role;
import lombok.Builder;

@Builder
public record DtoUser(
        Long id,
        String fullName,
        String email,
        Role role
) {

}
