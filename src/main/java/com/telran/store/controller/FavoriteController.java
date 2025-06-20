package com.telran.store.controller;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.entity.Favorite;
import com.telran.store.mapper.FavoriteMapper;
import com.telran.store.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public Set<FavoriteResponseDto> getAll () {
        return favoriteMapper.toDtoSet(favoriteService.getAll());
    }

    @PostMapping
    public FavoriteResponseDto addToFavorite(@RequestBody FavoriteCreateDto favorite) {
        return favoriteMapper.toDto(favoriteService.save(favoriteMapper.toEntity(favorite)));
    }
}
