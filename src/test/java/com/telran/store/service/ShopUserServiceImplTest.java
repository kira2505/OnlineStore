package com.telran.store.service;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserDto;
import com.telran.store.entity.ShopUser;
import com.telran.store.enums.Role;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.mapper.ShopUserMapper;
import com.telran.store.repository.ShopUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

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
        ShopUser user = ShopUser.builder().id(1L).name("Alex").role(Role.ROLE_USER).passwordHash("1234").build();

        String encodedPassword = "$2a$10$TPephM0u8ELy6Z54uWa6iuT";

        when(passwordEncoder.encode("1234"))
                .thenReturn(encodedPassword);

        ShopUser savedUser = ShopUser.builder()
                .id(1L)
                .name("Alex")
                .passwordHash(encodedPassword)
                .role(Role.ROLE_USER)
                .build();

        when(shopUserRepository.save(any())).thenReturn(savedUser);

        ShopUser result = shopUserService.create(user);

        assertEquals(1L, result.getId());
        assertEquals("Alex", result.getName());
        assertEquals(encodedPassword, result.getPasswordHash());
        assertEquals(Role.ROLE_USER, result.getRole());

        verify(passwordEncoder).encode("1234");
        verify(shopUserRepository).save(any(ShopUser.class));
    }

    @Test
    void testEditUser() {
        ShopUser user = ShopUser.builder().id(1L).name("Alex").role(Role.ROLE_USER).build();

        ShopUserDto userDto = new ShopUserDto();
        userDto.setName("Max");

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        doAnswer(invocation -> {
            ShopUser target = invocation.getArgument(0);
            ShopUserDto source = invocation.getArgument(1);
            target.setName(source.getName());
            return null;
        }).when(shopUserMapper).toUpdateEntity(any(), any());

        when(shopUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShopUser result = shopUserService.edit(userDto);

        verify(shopUserMapper).toUpdateEntity(user, userDto);
        verify(shopUserRepository).save(user);
        assertEquals("Max", result.getName());
    }

    @Test
    void testGetUserByEmail() {
        String email = "email@example.com";
        ShopUser user = ShopUser.builder().id(1L).email(email).build();

        when(shopUserRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ShopUser result = shopUserService.getByEmail(email);

        assertEquals(email, result.getEmail());
        verify(shopUserRepository).findByEmail(email);
    }

    @Test
    void testGetUserByEmailNotFound() {
        String email = "email@example.com";

        when(shopUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> shopUserService.getByEmail(email));
    }
    
    @Test
    void testGetShopUser() {
        ShopUser user = ShopUser.builder().id(1L).name("Alex").role(Role.ROLE_USER).build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        ShopUser result = shopUserService.getShopUser();

        assertEquals(user, result);
    }
}