package com.telran.store.controller;

import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.mapper.FavoriteMapper;
import com.telran.store.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteMapper favoriteMapper;

    @MockBean
    private ProductResponseDto productResponseDto;

    @Test
    void testGetProductAll() throws Exception {
        List<Favorite> favorites = Arrays.asList(
                Favorite.builder()
                        .id(1L).products(new Product()).build(),
                Favorite.builder()
                        .id(2L).products(new Product()).build());
        List<FavoriteResponseDto> favoriteResponseDtos = favorites.stream()
                .map(favorite -> new FavoriteResponseDto(favorite.getId(),
                        new ProductResponseDto(favorite.getProducts().getId(),
                                favorite.getProducts().getName(),
                                favorite.getProducts().getDescription(),
                                favorite.getProducts().getPrice(),
                                favorite.getProducts().getImageUrl(),
                                favorite.getProducts().getDiscountPrice()))).toList();

        when(favoriteService.getAll()).thenReturn(favorites);
        when(favoriteMapper.toDtoList(any())).thenReturn(favoriteResponseDtos);

        String contentAsString = mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(true);
    }
}