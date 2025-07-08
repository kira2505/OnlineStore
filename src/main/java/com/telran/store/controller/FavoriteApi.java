package com.telran.store.controller;

import com.telran.store.dto.FavoriteCreateDto;
import com.telran.store.dto.FavoriteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@Tag(name = "Favorites", description = "Operations related to user's favorite products")
public interface FavoriteApi {

    @Operation(summary = "Get all favorites (Only for admins)", description = "Retrieves a list of all favorite items")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of favorites",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
//    @ApiResponse(responseCode = "404", description = "Favorites not found")
    public Set<FavoriteResponseDto> getAll();

    @Operation(summary = "Get all favorites", description = "Retrieves a list of all favorite items for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of favorites",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
//    @ApiResponse(responseCode = "401", description = "User unauthorized")
//    @ApiResponse(responseCode = "404", description = "Favorites not found")
    public Set<FavoriteResponseDto> getAllByUserId();

    @Operation(summary = "Add a product to favorites", description = "Adds a specific product to the authenticated user's favorites.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Add product to favorite", required = true,
            content = @Content(schema = @Schema(implementation = FavoriteCreateDto.class)))
    @ApiResponse(responseCode = "201", description = "Successfully added the product to favorites",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
//    @ApiResponse(responseCode = "404", description = "Product with the given ID not found")
    public FavoriteResponseDto addToFavorite(@Valid @RequestBody FavoriteCreateDto dto);

    @Operation(summary = "Remove a favorite item", description = "Removes a specific favorite item based on its unique identifier.")
    @Parameter(name = "id", description = "ID of the favorite item to remove", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "123"))
    @ApiResponse(responseCode = "200", description = "Successfully removed the favorite item")
    @ApiResponse(responseCode = "404", description = "Favorite item with the given ID not found")
    void delete(@PathVariable Long id);
}
