package com.telran.store.mapper;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.entity.Favorite;
import org.mapstruct.Mapper;

import java.util.Set;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface FavoriteMapper {

    FavoriteResponseDto toDto(Favorite favorite);

    Favorite toEntity(FavoriteCreateDto favoriteCreateDto);

    Set<FavoriteResponseDto> toDtoSet(Set<Favorite> favorites);
}
