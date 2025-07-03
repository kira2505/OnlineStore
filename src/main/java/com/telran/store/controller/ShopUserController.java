package com.telran.store.controller;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserDto;
import com.telran.store.dto.ShopUserResponseDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.service.ShopUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ShopUserController {

    @Autowired
    private ShopUserService shopUserService;

    @Autowired
    private ShopUserMapper shopUserMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopUserResponseDto create(@RequestBody ShopUserCreateDto shopUserCreateDto) {
        return shopUserMapper.toDto(shopUserService.create(shopUserMapper.toEntity(shopUserCreateDto)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ShopUserResponseDto> getAll() {
        return shopUserMapper.toDtoList(shopUserService.getAll());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ShopUserResponseDto getById(@PathVariable long id) {
        return shopUserMapper.toDto(shopUserService.getById(id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable long id) {
        shopUserService.deleteById(id);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShopUserResponseDto edit(@RequestBody ShopUserDto shopUserDto) {
        return shopUserMapper.toDto(shopUserService.edit(shopUserDto));
    }
}
