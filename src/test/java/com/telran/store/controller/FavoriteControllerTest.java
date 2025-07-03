package com.telran.store.controller;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Favorite;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.mapper.FavoriteMapper;
import com.telran.store.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteMapper favoriteMapper;

    @Test
    void testGetProductAll() throws Exception {
        Set<Favorite> favorites = Set.of(
                Favorite.builder()
                        .id(1L).products(Product.builder().id(10L).name("P1").build()).build(),
                Favorite.builder()
                        .id(2L).products(Product.builder().id(20L).name("P2").build()).build());
        Set<FavoriteResponseDto> favoriteResponseDtos = favorites.stream()
                .map(favorite -> new FavoriteResponseDto(favorite.getId(),
                        new ProductResponseDto(favorite.getProducts().getId(),
                                favorite.getProducts().getName(),
                                favorite.getProducts().getDescription(),
                                favorite.getProducts().getPrice(),
                                favorite.getProducts().getImageUrl(),
                                favorite.getProducts().getDiscountPrice()))).collect(Collectors.toSet());

        when(favoriteService.getAll()).thenReturn(favorites);
        when(favoriteMapper.toDtoSet(any())).thenReturn(favoriteResponseDtos);

        String contentAsString = mockMvc.perform(get("/favorites/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(containsInAnyOrder(1, 2)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(true);
    }

    @Test
    void getAllByUserId_shouldReturnFavoriteResponseDtos() throws Exception {
        ShopUser user = new ShopUser();
        user.setId(1L);

        Product product = new Product();
        product.setId(2L);

        Favorite favorite = new Favorite(product, user);
        favorite.setId(10L);

        FavoriteResponseDto responseDto = new FavoriteResponseDto(10L,
                new ProductResponseDto(2L, null, null, null, null, null)
        );

        Set<Favorite> favorites = Set.of(favorite);
        Set<FavoriteResponseDto> responseDtos = Set.of(responseDto);

        when(favoriteService.getAllByUserId()).thenReturn(favorites);
        when(favoriteMapper.toDtoSet(favorites)).thenReturn(responseDtos);


        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].products.id").value(2));
    }


    @Test
    void addToFavorite_shouldReturnCreatedFavoriteDto() throws Exception {
        FavoriteCreateDto createDto = new FavoriteCreateDto();
        createDto.setProductId(2L);

        ShopUser user = new ShopUser();
        user.setId(1L);

        Product product = new Product();
        product.setId(2L);

        Favorite savedFavorite = new Favorite(product, user);
        savedFavorite.setId(11L);

        FavoriteResponseDto responseDto = new FavoriteResponseDto(11L,
                new ProductResponseDto(2L, null, null, null, null, null)
        );


        when(favoriteService.save(createDto)).thenReturn(savedFavorite);
        when(favoriteMapper.toDto(savedFavorite)).thenReturn(responseDto);

        // When
        String jsonRequest = """
            {
                "productId": 2
            }
            """;

        // Выполняем POST-запрос и проверяем ответ
        mockMvc.perform(post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.products.id").value(2));
    }
}