package com.telran.store.mapper;

import com.telran.store.dto.FavoriteResponseDto;
import com.telran.store.entity.Favorite;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface FavoriteMapper {

    FavoriteResponseDto toDto(Favorite favorite);

    List<FavoriteResponseDto> toDtoList(List<Favorite> favorites);
}
