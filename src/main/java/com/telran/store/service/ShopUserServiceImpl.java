package com.telran.store.service;

import com.telran.store.dto.ShopUserDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.enums.Role;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.repository.ShopUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        ShopUser savedUser = shopUserRepository.save(shopUser);
        log.info("A new user has registered, user ID: {}", savedUser.getId());
        return savedUser;
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
        log.info("User with ID: {} has been deleted", user.getId());
        shopUserRepository.deleteById(user.getId());
    }

    @Override
    public ShopUser edit(ShopUserDto shopUser) {
        ShopUser user = getShopUser();
        shopUserMapper.toUpdateEntity(user, shopUser);
        log.info("User with ID: {} updated", user.getId());
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
        log.info("Assigned admin status for user with ID: {}", user.getId());
        return shopUserRepository.save(user);
    }
}
