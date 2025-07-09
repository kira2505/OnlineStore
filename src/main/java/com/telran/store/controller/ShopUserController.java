package com.telran.store.controller;

import com.telran.store.dto.LoginRequestDto;
import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserDto;
import com.telran.store.dto.ShopUserResponseDto;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.service.ShopUserService;
import com.telran.store.service.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ShopUserController implements ShopUserApi {

    @Autowired
    private ShopUserService shopUserService;

    @Autowired
    private ShopUserMapper shopUserMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public ShopUserResponseDto create(@Valid @RequestBody ShopUserCreateDto shopUserCreateDto) {
        return shopUserMapper.toDto(shopUserService.create(shopUserMapper.toEntity(shopUserCreateDto)));
    }

    @Override
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        return authenticationService.login(loginRequestDto);
    }

    @Override
    public List<ShopUserResponseDto> getAll() {
        return shopUserMapper.toDtoList(shopUserService.getAll());
    }

    @Override
    public ShopUserResponseDto getById(@PathVariable long id) {
        return shopUserMapper.toDto(shopUserService.getById(id));
    }

    @Override
    public void deleteById(@PathVariable long id) {
        shopUserService.deleteById(id);
    }

    @Override
    public ShopUserResponseDto edit(@Valid @RequestBody ShopUserDto shopUserDto) {
        return shopUserMapper.toDto(shopUserService.edit(shopUserDto));
    }

    @Override
    public ShopUserResponseDto assignAdminStatus(@PathVariable Long id) {
        return shopUserMapper.toDto(shopUserService.assignAdminStatus(id));
    }
}
