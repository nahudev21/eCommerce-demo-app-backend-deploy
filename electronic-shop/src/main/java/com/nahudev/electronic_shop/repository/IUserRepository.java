package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    public boolean existsByEmail(String email);

    public User findByEmail(String email);
}
