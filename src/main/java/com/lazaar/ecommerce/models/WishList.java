package com.lazaar.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Wishlist")
public class WishList {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "wishlist_seq")
    @SequenceGenerator(
            name = "wishlist_seq",
            sequenceName = "wishlist_seq"
    )

    private Long id;

    private Long userId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"productStatus", "category", "wishList"})
    private Product product;
}
