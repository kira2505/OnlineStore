package com.telran.store.service;

import com.telran.store.entity.ShopUser;
import com.telran.store.repository.ShopUserRepository;
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
class ShopUserServiceImplTest {

    @Mock
    private ShopUserRepository shopUserRepository;

    @InjectMocks
    private ShopUserServiceImpl shopUserService;

    @Test
    void testGetAllUsers() {
        List<ShopUser> shopUsers = Arrays.asList(
                ShopUser.builder()
                        .id(1L).name("Alex").build(),
                ShopUser.builder()
                        .id(2L).name("Max").build());

        when(shopUserRepository.findAll()).thenReturn(shopUsers);

        List<ShopUser> users = shopUserService.getAll();
        assertEquals(2, users.size());
    }
}