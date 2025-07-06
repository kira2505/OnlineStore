package com.telran.store.service;

import com.telran.store.entity.ShopUser;
import com.telran.store.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopUserDetailServiceTest {
    @Mock
    private ShopUserService shopUserService;

    @InjectMocks
    private ShopUserDetailService shopUserDetailService;

    @Test
    void testLoadUserByUsername() {
        String email = "example@gmail.com";
        ShopUser shopUser = ShopUser.builder()
                .id(1L)
                .email(email)
                .passwordHash("1234")
                .build();

        when(shopUserService.getByEmail(email)).thenReturn(shopUser);

        UserDetails userDetails = shopUserDetailService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());

        verify(shopUserService).getByEmail(email);
    }
}