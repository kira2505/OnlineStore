package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category Management", description = "Create, update, and delete product categories.")
public interface CategoryApi {

    @Operation(summary = "Create new category (Only for admins)",
            description = "Returns a new category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "Garden Tools"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string"),
                            examples = @ExampleObject(value = "\"Category name must not be blank\"")
                    ))})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Created category", required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryCreateDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "name": "Garden Tools"
                            }
                            """)))
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    CategoryDto create(CategoryCreateDto categoryCreateDto);

    @Operation(summary = "Get all categories",
            description = "Returns all categories.")
    @ApiResponse(responseCode = "200", description = "List of categories returned",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDto.class)),
                    examples = @ExampleObject(value = """
                            [
                              {
                                "id": 1,
                                "name": "Garden Tools"
                              },
                              {
                                "id": 2,
                                "name": "Garden Furniture"
                              }
                            ]
                            """)))
    @GetMapping
    List<CategoryResponseDto> getAll();

    @Operation(summary = "Get category by ID",
            description = "Returns the category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category returned",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "Garden Tools"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(examples = @ExampleObject(value = "\"Category with ID 1 not found\"")))})
    @Parameter(name = "id", description = "Unique category identifier.", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @GetMapping("{id}")
    CategoryResponseDto getById(long id);

    @Operation(summary = "Delete category by ID (Only for admins)",
            description = "Delete the category by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(examples = @ExampleObject(value = "\"Category with ID 1 not found\"")))})
    @Parameter(name = "id", description = "Unique category identifier.", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    void deleteById(long id);

    @Operation(summary = "Edit category (Only for admins)",
            description = "Fields that can be changed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category returned",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "Fertilizers"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid data",
                    content = @Content(examples = @ExampleObject(value = "\"Category name must not be blank\""))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(examples = @ExampleObject(value = "\"Category with ID 1 not found\"")))})
    @Parameter(name = "id", description = "Unique category identifier.", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated category data", required = true,
            content = @Content(schema = @Schema(implementation = CategoryResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "name": "Fertilizers"
                            }
                            """)))
    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    CategoryResponseDto edit(long id, CategoryCreateDto category);
}
