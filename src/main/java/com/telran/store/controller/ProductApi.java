package com.telran.store.controller;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Product Management", description = "Operations for managing products")
public interface ProductApi {

    @Operation(summary = "Create new product (Only for admins)", description = "Return a new product.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product details", required = true,
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "201", description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "400", description = "Invalid product data")
//    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    ProductResponseDto createProduct(@Valid @RequestBody ProductCreateDto dto);

    @Operation(summary = "Get all products", description = "Return all products with filtering")
    @Parameters({
            @Parameter(name = "category", description = "Category name", required = false, example = "Garden", schema = @Schema(type = "string")),
            @Parameter(name = "minPrice", description = "Minimal price", required = false, example = "10.00", schema = @Schema(type = "bigdecimal")),
            @Parameter(name = "maxPrice", description = "Maximal price", required = false, example = "20.00", schema = @Schema(type = "bigdecimal")),
            @Parameter(name = "discount", description = "Discount value", required = false, example = "true", schema = @Schema(type = "boolean")),
            @Parameter(name = "sort", description = "Sort by id, price and name", required = false, schema = @Schema(type = "string", defaultValue = "id"))})
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product filter or sort")
    List<ProductResponseDto> getAll(@RequestParam(name = "category", required = false) String category,
                                    @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                    @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                    @RequestParam(name = "discount", required = false) Boolean discount,
                                    @RequestParam(name = "sort", required = false, defaultValue = "id") String sort);

    @Operation(summary = "Get product by ID", description = "Return the product by product ID.")
    @Parameter(name = "id", description = "ID of the product to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "404", description = "Product not found")
    ProductResponseDto getById(@PathVariable long id);

    @Operation(summary = "Delete product by ID (Only for admins)", description = "Delete the product by their product ID.")
    @Parameter(name = "id", description = "ID of the product to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "404", description = "Product not found")
    void deleteById(@PathVariable long id);

    @Operation(summary = "Edit product (Only for admins)", description = "Update an existing product with new data")
    @Parameter(name = "id", description = "ID of the product to update", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product details", required = true,
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "400", description = "Invalid product data")
//    @ApiResponse(responseCode = "404", description = "Product not found")
    ProductResponseDto edit(@PathVariable Long id, @Valid @RequestBody ProductCreateDto dto);

    @Operation(summary = "Get product of the day", description = "Retrieves the current product of the day")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product of the day",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
//    @ApiResponse(responseCode = "404", description = "Product of the day not found")
    ProductResponseDto getDailyProduct();
}
