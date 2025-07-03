package com.telran.store.configuration;

import com.telran.store.entity.ShopUser;
import com.telran.store.enums.Role;
import com.telran.store.repository.ShopUserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DbInitializer {

    private final ShopUserRepository shopUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (shopUserRepository.findAll().isEmpty()) {
            List<ShopUser> users = List.of(
                    ShopUser.builder().name("Admin User").email("admin@example.com").phoneNumber("+79998887766")
                            .passwordHash(passwordEncoder.encode("1234")).role(Role.ROLE_ADMIN).build(),

                    ShopUser.builder().name("Maria Petrova").email("maria.petrova@example.com").phoneNumber("+79161234567")
                            .passwordHash(passwordEncoder.encode("1234")).role(Role.ROLE_USER).build(),

                    ShopUser.builder().name("Ivan Ivanov").email("ivan.ivanov@gmail.com").phoneNumber("+79261112233")
                            .passwordHash(passwordEncoder.encode("1234")).role(Role.ROLE_USER).build(),

                    ShopUser.builder().name("Dmitry Orlov").email("d.orlov@mail.ru").phoneNumber("+79301234567")
                            .passwordHash(passwordEncoder.encode("1234")).role(Role.ROLE_USER).build()
            );
            shopUserRepository.saveAll(users);
        }
    }
}
