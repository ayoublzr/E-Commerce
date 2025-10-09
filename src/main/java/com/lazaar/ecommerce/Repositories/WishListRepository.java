package com.lazaar.ecommerce.Repositories;

import com.lazaar.ecommerce.models.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    @Query(
            value = "SELECT w FROM WishList w WHERE w.userId = :userId"
    )
    List<WishList> findAllWishListById(Long userId);
}
