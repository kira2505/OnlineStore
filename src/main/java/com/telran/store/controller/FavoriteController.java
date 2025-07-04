package com.telran.store.controller;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Favorite;
import com.telran.store.mapper.FavoriteMapper;
import com.telran.store.service.FavoriteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Set<FavoriteResponseDto> getAll () {
        return favoriteMapper.toDtoSet(favoriteService.getAll());
    }

    @GetMapping
    public Set<FavoriteResponseDto> getAllByUserId() {
        return favoriteMapper.toDtoSet(favoriteService.getAllByUserId());
    }

    @PostMapping
    public FavoriteResponseDto addToFavorite(@Valid @RequestBody FavoriteCreateDto dto) {
        return favoriteMapper.toDto(favoriteService.save(dto));
    }
}
