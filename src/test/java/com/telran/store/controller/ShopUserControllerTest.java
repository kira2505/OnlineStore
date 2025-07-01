package com.telran.store.controller;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserResponseDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.service.ShopUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShopUserController.class)
class ShopUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopUserService shopUserService;

    @MockBean
    private ShopUserMapper shopUserMapper;

    @Test
    void testGetAllUsers() throws Exception {
        List<ShopUser> shopUsers = Arrays.asList(
                ShopUser.builder()
                        .id(1L).name("Alex").email("alex@gmail.com").phoneNumber("111222").build(),
                ShopUser.builder()
                        .id(2L).name("Max").email("max@gmail.com").phoneNumber("333444").build());
        List<ShopUserResponseDto> shopUserResponseDtos = shopUsers.stream()
                .map(shopUser -> new ShopUserResponseDto(shopUser.getId(),
                        shopUser.getName(), shopUser.getEmail(), shopUser.getPhoneNumber()))
                .toList();

        when(shopUserService.getAll()).thenReturn(shopUsers);
        when(shopUserMapper.toDtoList(any())).thenReturn(shopUserResponseDtos);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testGetUserById() throws Exception {
        ShopUser shopUser = ShopUser.builder().id(1L).name("Alex").email("alex@gmail.com").phoneNumber("111222").build();
        ShopUserResponseDto shopUserDto = new ShopUserResponseDto(1L, "Alex", "alex@gmail.com", "111222");

        when(shopUserService.getById(shopUser.getId())).thenReturn(shopUser);
        when(shopUserMapper.toDto(any())).thenReturn(shopUserDto);

        mockMvc.perform(get("/users/" + shopUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shopUser.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testDeleteUserById() throws Exception {
        ShopUser shopUser = ShopUser.builder().id(1L).name("Alex").email("alex@gmail.com").phoneNumber("111222").build();

        doNothing().when(shopUserService).deleteById(shopUser.getId());

        mockMvc.perform(delete("/users/" + shopUser.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testCreateUser() throws Exception {
        ShopUser user = ShopUser.builder().id(1L).name("Max").email("max@gmail.com").phoneNumber("44445555666").build();
        ShopUserResponseDto userResponseDto =
                new ShopUserResponseDto(1L, "Max", "max@gmail.com", "44445555666");

        when(shopUserService.create(any())).thenReturn(user);
        when(shopUserMapper.toDto(any())).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Max\", \"email\": \"max@gmail.com\", \"phoneNumber\": \"44445555666\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testEditUser() throws Exception {
        ShopUser shopUser = ShopUser.builder().id(1L).name("Max").email("max@gmail.com").phoneNumber("44445555666").build();

        ShopUserCreateDto shopUserCreateDto = new ShopUserCreateDto();
        shopUserCreateDto.setName("Alex");

        when(shopUserService.edit(eq(1L), any(ShopUserCreateDto.class))).thenReturn(shopUser);
        when(shopUserMapper.toDto(shopUser)).thenReturn(new ShopUserResponseDto());

        mockMvc.perform(patch("/users/" + shopUser.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\": \"Alex\", \"email\": \"max@gmail.com\", \"phoneNumber\": \"44445555666\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(shopUserService).edit(eq(1L), any(ShopUserCreateDto.class));
        verify(shopUserMapper).toDto(shopUser);
    }
}