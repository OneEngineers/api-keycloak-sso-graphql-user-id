package com.api.sso.auth.repositories;

import com.api.sso.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositories extends JpaRepository <UserEntity, Long>{
}
