package com.api.sso.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.sso.auth.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    Optional<UserEntity> findById(Long id);

}
