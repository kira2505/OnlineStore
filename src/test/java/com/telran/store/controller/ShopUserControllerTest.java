package com.telran.store.controller;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserResponseDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.service.ShopUserService;
import com.telran.store.service.security.AuthenticationService;
import com.telran.store.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ShopUserController.class)
class ShopUserControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopUserService shopUserService;

    @MockBean
    private ShopUserMapper shopUserMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void testGetAllUsers() throws Exception {
        List<ShopUser> shopUsers = List.of(
                ShopUser.builder().id(1L).name("Alex").email("alex@gmail.com").phoneNumber("111222").build(),
                ShopUser.builder().id(2L).name("Max").email("max@gmail.com").phoneNumber("333444").build()
        );
        List<ShopUserResponseDto> responseDtos = shopUsers.stream()
                .map(user -> new ShopUserResponseDto(user.getId(), user.getName(),
                        user.getEmail(), user.getPhoneNumber()))
                .toList();

        when(shopUserService.getAll()).thenReturn(shopUsers);
        when(shopUserMapper.toDtoList(shopUsers)).thenReturn(responseDtos);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testGetUserById() throws Exception {
        long userId = 1L;
        ShopUser shopUser = ShopUser.builder()
                .id(userId)
                .name("Alex")
                .email("alex@gmail.com")
                .phoneNumber("111222")
                .build();

        ShopUserResponseDto dto = new ShopUserResponseDto(userId, "Alex", "alex@gmail.com", "111222");

        when(shopUserService.getById(userId)).thenReturn(shopUser);
        when(shopUserMapper.toDto(shopUser)).thenReturn(dto);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.email").value("alex@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("111222"));

        verify(shopUserService).getById(userId);
        verify(shopUserMapper).toDto(shopUser);
    }

    @Test
    void testDeleteUserById() throws Exception {
        doNothing().when(shopUserService).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateUser() throws Exception {
        ShopUser user = ShopUser.builder().id(1L).name("Max").email("max@gmail.com").phoneNumber("+380123444567").build();
        ShopUserResponseDto userResponseDto =
                new ShopUserResponseDto(1L, "Max", "max@gmail.com", "+380123444567");

        when(shopUserService.create(any())).thenReturn(user);
        when(shopUserMapper.toDto(any())).thenReturn(userResponseDto);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Max\", \"email\": \"max@gmail.com\", \"phoneNumber\": \"+380123444567\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateUserInvalidEmail() throws Exception {
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Max",
                          "email": "email",
                          "phoneNumber": "44445555666"
                        }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEditUser() throws Exception {
        ShopUser shopUser = ShopUser.builder().id(1L).name("Max").email("max@gmail.com").phoneNumber("+380123444567").build();

        ShopUserCreateDto shopUserCreateDto = new ShopUserCreateDto();
        shopUserCreateDto.setName("Alex");

        when(shopUserService.edit(any())).thenReturn(shopUser);
        when(shopUserMapper.toDto(shopUser)).thenReturn(new ShopUserResponseDto());

        mockMvc.perform(patch("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\": \"Alex\", \"email\": \"max@gmail.com\", \"phoneNumber\": \"+380123444567\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}