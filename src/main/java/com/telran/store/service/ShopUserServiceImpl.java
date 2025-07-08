package com.telran.store.service;

import com.telran.store.dto.ShopUserDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.enums.Role;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.repository.ShopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopUserServiceImpl implements ShopUserService {

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private ShopUserMapper shopUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ShopUser create(ShopUser shopUser) {
        shopUser.setPasswordHash(passwordEncoder.encode(shopUser.getPassword()));
        shopUser.setRole(Role.ROLE_USER);
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
        ShopUser user = getById(id);
        shopUserRepository.deleteById(user.getId());
    }

    @Override
    public ShopUser edit(ShopUserDto shopUser) {
        ShopUser user = getShopUser();
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

    @Override
    public ShopUser assignAdminStatus(Long userId) {
        ShopUser user = getById(userId);
        user.setRole(Role.ROLE_ADMIN);
        return shopUserRepository.save(user);
    }
}
