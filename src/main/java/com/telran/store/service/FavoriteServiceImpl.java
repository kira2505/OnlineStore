package com.telran.store.service;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.FavoriteAlreadyExistsException;
import com.telran.store.repository.FavoriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
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

        boolean exists = favoriteRepository.existsByProductsIdAndShopUserId(productById.getId(), userById.getId());
        if (exists) {
            throw new FavoriteAlreadyExistsException("This product is already in your favorites.");
        }

        Favorite save = favoriteRepository.save(new Favorite(productById, userById));
        log.debug("Saving new favorite: {}", dto);
        return save;
    }

    @Override
    public void delete(Long id) {
        Favorite favorite = favoriteRepository.findByShopUser_IdAndProducts_Id(shopUserService.getShopUser().getId(), id);
        favoriteRepository.deleteById(favorite.getId());
        log.debug("Favorite deleted: {}", favorite);
    }
}
