package com.telran.store.mapper;

import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.entity.Favorite;
import org.mapstruct.Mapper;

import java.util.Set;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface FavoriteMapper {

    FavoriteResponseDto toDto(Favorite favorite);

    Set<FavoriteResponseDto> toDtoSet(Set<Favorite> favorites);
}
