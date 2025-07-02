package com.telran.store.service;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.repository.ShopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopUserServiceImpl implements ShopUserService {

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private ShopUserMapper shopUserMapper;

    @Override
    public ShopUser create(ShopUser shopUser) {
        return shopUserRepository.save(shopUser);
    }

    @Override
    public List<ShopUser> getAll() {
        return shopUserRepository.findAll();
    }

    @Override
    public ShopUser getById(long id) {
        return shopUserRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("User with ID " + id + " not found."));
    }

    @Override
    public void deleteById(long id) {
        shopUserRepository.deleteById(id);
    }

    @Override
    public ShopUser edit(long id, ShopUserCreateDto shopUser) {
        ShopUser user = getById(id);

        shopUserMapper.toUpdateEntity(user, shopUser);
        return shopUserRepository.save(user);
    }

    @Override
    public ShopUser getByEmail(String email) {
        return shopUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }

    public ShopUser getShopUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (ShopUser) authentication.getPrincipal();
    }
}
