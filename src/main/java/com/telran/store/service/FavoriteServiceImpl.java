package com.telran.store.service;

import com.telran.store.entity.Favorite;
import com.telran.store.exception.FavoriteNotFoundException;
import com.telran.store.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService{

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public List<Favorite> getAll() {
        return favoriteRepository.findAll();
    }

    @Override
    public Favorite get(long product_id) {
        return favoriteRepository.findById(product_id).orElseThrow(() -> new FavoriteNotFoundException("Favorite not found"));
    }
}
