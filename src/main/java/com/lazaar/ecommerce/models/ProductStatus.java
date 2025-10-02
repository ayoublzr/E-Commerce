package com.lazaar.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;
@NoArgsConstructor
@Data
@Entity
public class ProductStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "status_name")
    private String statusName;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productStatus")
    private Set<Product> products;
}
