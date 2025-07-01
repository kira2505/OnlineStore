package com.telran.store.service;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.repository.ShopUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopUserServiceImplTest {

    @Mock
    private ShopUserRepository shopUserRepository;

    @InjectMocks
    private ShopUserServiceImpl shopUserService;

    @Mock
    private ShopUserMapper shopUserMapper;

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

    @Test
    void testGetUserById() {
        ShopUser user = ShopUser.builder().id(1L).name("Alex").build();

        long invalidId = 123L;
        when(shopUserRepository.findById(invalidId)).thenReturn(Optional.empty());

        when(shopUserRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertEquals(user, shopUserService.getById(user.getId()));

        assertThrows(UserNotFoundException.class, () -> shopUserService.getById(invalidId));
    }

    @Test
    void testDeleteUserById() {
        ShopUser user = ShopUser.builder().id(1L).name("Alex").build();

        doNothing().when(shopUserRepository).deleteById(user.getId());

        shopUserService.deleteById(user.getId());

        verify(shopUserRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void testCreateUser() {
        ShopUser user = ShopUser.builder().id(1L).name("Alex").build();

        when(shopUserRepository.save(user)).thenReturn(user);

        assertEquals(user, shopUserService.create(user));
    }

    @Test
    void testEditUser() {
        ShopUser user = ShopUser.builder().id(1L).name("Alex").build();

        ShopUserCreateDto userCreateDto = new ShopUserCreateDto();
        userCreateDto.setName("Max");

        when(shopUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(shopUserRepository.save(any())).thenReturn(user);

        doAnswer(invocation -> {
            ShopUser target = invocation.getArgument(0);
            ShopUserCreateDto source = invocation.getArgument(1);
            target.setName(source.getName());
            return null;
        }).when(shopUserMapper).toUpdateEntity(any(), any());

        ShopUser result = shopUserService.edit(1L, userCreateDto);

        verify(shopUserMapper).toUpdateEntity(user, userCreateDto);
        verify(shopUserRepository).save(user);

        assertEquals("Max", result.getName());
    }
}