package com.telran.store.mapper;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserResponseDto;
import com.telran.store.entity.ShopUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShopUserMapper {

    ShopUser toEntity(ShopUserCreateDto shopUserCreateDto);

    ShopUserResponseDto toDto(ShopUser shopUser);

    List<ShopUserResponseDto> toDtoList(List<ShopUser> shopUsers);
}
