package com.lazaar.ecommerce.DTO.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Data
public class ProductDto {
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private Integer unitInStock;
    private boolean active;
    private String imageBase64;
    private String imageContentType;
    private Long categoryId;
    private Long productStatusId;
}
