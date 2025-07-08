package com.telran.store.repository;

import com.telran.store.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Set<Favorite> findByShopUser_Id(Long shopUserId);

    Favorite findByShopUser_IdAndProducts_Id(Long shopUserId, Long productsId);
}
