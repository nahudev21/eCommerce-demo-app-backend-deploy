package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

   public boolean existsByName(String name);

   public Role findByName(String name);

    List<Role> findByNameIn(List<String> roles);
}
