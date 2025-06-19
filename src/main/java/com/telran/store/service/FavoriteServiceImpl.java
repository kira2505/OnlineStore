package com.telran.store.service;

import com.telran.store.entity.Favorite;
import com.telran.store.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FavoriteServiceImpl implements FavoriteService{

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public Set<Favorite> getAll() {
        return new HashSet<>(favoriteRepository.findAll());
    }
}
