package com.lazaar.ecommerce.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EditUserDto {
    private String fulltName;
    private String email;
}
