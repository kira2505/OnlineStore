package com.telran.store.service;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.entity.Favorite;

import java.util.List;
import java.util.Set;

public interface FavoriteService {

    Set<Favorite> getAll();

    Favorite save(FavoriteCreateDto favorite);
}
