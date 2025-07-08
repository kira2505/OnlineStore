package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Category", description = "Category operations")
public interface CategoryApi {

    @Operation(summary = "Create new category (Only for admins)",
            description = "Returns a new category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Created category", required = true,
            content = @Content(schema = @Schema(implementation = CategoryCreateDto.class)))
    CategoryDto create(CategoryCreateDto categoryCreateDto);

    @Operation(summary = "Get all categories",
            description = "Returns all categories.")
    @ApiResponse(responseCode = "200", description = "List of categories returned",
            content = @Content(schema = @Schema(implementation = CategoryResponseDto.class)))
    List<CategoryResponseDto> getAll();

    @Operation(summary = "Get category by ID (Only for admins)",
            description = "Returns the category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category returned",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")})
    @Parameter(name = "id", description = "Unique category identifier.", required = true,
            schema =  @Schema(type = "integer", format = "int64", example = "1"))
    CategoryResponseDto getById(long id);

    @Operation(summary = "Delete category by ID (Only for admins)",
            description = "Delete the category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found")})
    @Parameter(name = "id", description = "Unique category identifier.", required = true,
            schema =  @Schema(type = "integer", format = "int64", example = "1"))
    void deleteById(long id);

    @Operation(summary = "Edit category (Only for admins)",
            description = "Fields that can be changed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Category not found")})
    @Parameter(name = "id", description = "Unique category identifier.", required = true,
            schema =  @Schema(type = "integer", format = "int64", example = "1"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated category data", required = true,
            content = @Content(schema = @Schema(implementation = CategoryCreateDto.class)))
    CategoryResponseDto edit(long id, CategoryCreateDto category);
}
