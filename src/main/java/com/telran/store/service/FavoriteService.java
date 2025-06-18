package com.telran.store.service;

import com.telran.store.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    List<Favorite> getAll();

    Favorite get(long product_id);
}
