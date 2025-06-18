package com.telran.store.repository;

import com.telran.store.entity.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopUserRepository extends JpaRepository<ShopUser, Long> {
}
