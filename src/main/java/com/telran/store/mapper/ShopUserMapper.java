package com.telran.store.mapper;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserDto;
import com.telran.store.dto.ShopUserResponseDto;
import com.telran.store.entity.ShopUser;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {FavoriteMapper.class})
public interface ShopUserMapper {

    ShopUser toEntity(ShopUserCreateDto shopUserCreateDto);

    @Mapping(source = "id", target = "id")
    ShopUserResponseDto toDto(ShopUser shopUser);

    List<ShopUserResponseDto> toDtoList(List<ShopUser> shopUsers);

    void toUpdateEntity(@MappingTarget ShopUser shopUser, ShopUserCreateDto dto);

    ShopUserDto toDtoUser(ShopUser shopUser);
}
