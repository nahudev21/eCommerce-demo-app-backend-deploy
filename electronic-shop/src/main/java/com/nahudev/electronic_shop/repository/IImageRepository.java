package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);
}
