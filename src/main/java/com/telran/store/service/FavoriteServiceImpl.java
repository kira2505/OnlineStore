package com.telran.store.service;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.CartNotFoundException;
import com.telran.store.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FavoriteServiceImpl implements FavoriteService{

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopUserService shopUserService;

    @Override
    public Set<Favorite> getAll() {
        return new HashSet<>(favoriteRepository.findAll());
    }

    @Override
    public Set<Favorite> getAllByUserId() {
        return favoriteRepository.findByShopUser_Id(shopUserService.getShopUser().getId());
    }

    @Override
    public Favorite save(FavoriteCreateDto dto) {
        ShopUser userById = shopUserService.getById(shopUserService.getShopUser().getId());
        Product productById = productService.getById(dto.getProductId());
        return favoriteRepository.save(new Favorite(productById, userById));
    }
}
