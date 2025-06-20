package com.telran.store.service;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
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
    public Favorite save(FavoriteCreateDto favorite) {
        ShopUser userById = shopUserService.getById(favorite.getUserId());
        Product productById = productService.getById(favorite.getProductId());
        return favoriteRepository.save(new Favorite(productById, userById));
    }
}
