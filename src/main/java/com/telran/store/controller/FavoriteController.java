package com.telran.store.controller;

import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.mapper.FavoriteMapper;
import com.telran.store.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteMapper favoriteMapper;

    @GetMapping("/{product_id}")
    public FavoriteResponseDto getFavorite(@RequestParam("id") long id) {
        return favoriteMapper.toDto(favoriteService.get(id));
    }

    @GetMapping
    public List<FavoriteResponseDto> getAll () {
        return favoriteMapper.toDtoList(favoriteService.getAll());
    }
}
