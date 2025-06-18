package com.telran.store.service;

import com.telran.store.entity.ShopUser;

import java.util.List;

public interface ShopUserService {

    ShopUser create(ShopUser shopUser);

    List<ShopUser> getAll();

    ShopUser getById(long id);

    void deleteById(long id);
}
