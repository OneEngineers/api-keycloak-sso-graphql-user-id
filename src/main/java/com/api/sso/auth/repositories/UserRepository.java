package com.api.sso.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.sso.auth.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
    public void delete(Long id);
    Optional<UserEntity> findById(Long id);
    public List<UserEntity> findAll();

}
