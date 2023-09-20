package com.atasoy.recipe.repo;

import com.atasoy.recipe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserNameAndDeletedFalse(String userName);

    boolean existsByUserName(String userName);
}
