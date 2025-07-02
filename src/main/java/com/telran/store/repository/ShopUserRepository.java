package com.telran.store.repository;

import com.telran.store.entity.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopUserRepository extends JpaRepository<ShopUser, Long> {

    Optional<ShopUser> findByEmail(String email);
}
