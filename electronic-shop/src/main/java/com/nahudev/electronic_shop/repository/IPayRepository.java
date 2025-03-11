package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPayRepository extends JpaRepository<Pay, Long> {
}
