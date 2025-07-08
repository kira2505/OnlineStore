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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ShopUserResponseDto create(@Valid @RequestBody ShopUserCreateDto shopUserCreateDto) {
        return shopUserMapper.toDto(shopUserService.create(shopUserMapper.toEntity(shopUserCreateDto)));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        return authenticationService.login(loginRequestDto);
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
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable long id) {
        shopUserService.deleteById(id);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ShopUserResponseDto edit(@Valid @RequestBody ShopUserDto shopUserDto) {
        return shopUserMapper.toDto(shopUserService.edit(shopUserDto));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ShopUserResponseDto assignAdminStatus(@PathVariable Long id) {
        return shopUserMapper.toDto(shopUserService.assignAdminStatus(id));
    }
}
