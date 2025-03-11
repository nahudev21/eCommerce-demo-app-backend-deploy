package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findByUserId(Long userId);
}
