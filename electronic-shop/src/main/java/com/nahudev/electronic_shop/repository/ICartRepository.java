package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long> {
    public Cart findByUserId(Long userId);
}
