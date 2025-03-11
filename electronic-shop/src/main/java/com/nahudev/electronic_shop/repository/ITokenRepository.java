package com.nahudev.electronic_shop.repository;

import com.nahudev.electronic_shop.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {

    public Optional<Token> findByAccessToken(String token);

    @Query("""
           select t from Token t inner join User u on t.user.id = u.id
           where u.id = :userId and (t.expired = false or t.revoked = false)
           """)
    public List<Token> findAllValidTokensByUser(Long userId);

}
