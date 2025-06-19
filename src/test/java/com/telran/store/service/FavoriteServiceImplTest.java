package com.telran.store.service;

import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.repository.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    void testGetAll() {
        List<Favorite> favorites = Arrays.asList(
                Favorite.builder()
                        .id(1L).products(new Product()).build(),
                Favorite.builder()
                        .id(2L).products(new Product()).build());

        when(favoriteRepository.findAll()).thenReturn(favorites);

        List<Favorite> result = favoriteService.getAll();
        assertEquals(result, favorites);
    }
}