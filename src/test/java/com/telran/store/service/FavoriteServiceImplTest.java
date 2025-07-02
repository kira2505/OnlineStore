package com.telran.store.service;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.repository.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ShopUserService shopUserService;

    @Mock
    private ProductService productService;

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

        Set<Favorite> result = favoriteService.getAll();
        HashSet<Favorite> all = new HashSet<>(favorites);
        assertEquals(result, all);
    }

    @Test
    void getAllByUserId_shouldReturnFavoritesForUser() {
        // Given
        Long userId = 1L;
        ShopUser user = new ShopUser();
        user.setId(userId);

        Product productOne = new Product();
        productOne.setId(1L);

        Product productTwo = new Product();
        productTwo.setId(2L);

        Favorite favoriteOne = new Favorite(productOne, user);
        Favorite favoriteTwo = new Favorite(productTwo, user);
        Set<Favorite> favorites = Set.of(favoriteOne, favoriteTwo);

        when(shopUserService.getShopUser()).thenReturn(user);
        when(favoriteRepository.findByShopUser_Id(userId)).thenReturn(favorites);

        // When
        Set<Favorite> result = favoriteService.getAllByUserId();

        // Then
        assertEquals(favorites, result);
        verify(shopUserService).getShopUser();
        verify(favoriteRepository).findByShopUser_Id(userId);
    }

    @Test
    void save_shouldCreateFavoriteSuccessfully() {
        // Given
        Long userId = 1L;
        Long productId = 2L;

        ShopUser user = new ShopUser();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);

        FavoriteCreateDto dto = new FavoriteCreateDto();
        dto.setProductId(productId);

        Favorite expected = new Favorite(product, user);

        when(shopUserService.getShopUser()).thenReturn(user);
        when(shopUserService.getById(userId)).thenReturn(user);
        when(productService.getById(productId)).thenReturn(product);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(expected);

        // When
        Favorite result = favoriteService.save(dto);

        // Then
        assertEquals(expected, result);
        verify(shopUserService).getShopUser();
        verify(shopUserService).getById(userId);
        verify(productService).getById(productId);
        verify(favoriteRepository).save(any(Favorite.class));
    }
}