package com.TNBtech.secure_product_api.repositories;

import com.TNBtech.secure_product_api.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {


    // Pour vérifier si un JWT actif existe pour un utilisateur
    Stream<Jwt> findAllByUserUsernameAndExpiredFalseAndRevokedFalse(String username);

    // Pour récupérer un seul token valide (utile pour logout)
    Optional<Jwt> findByUserUsernameAndExpiredAndRevoked(String username, boolean expired, boolean revoked);

    void deleteAllByExpiredAndRevoked(boolean expired, boolean revoked);
}
