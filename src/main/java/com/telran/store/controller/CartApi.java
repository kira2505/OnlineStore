package com.telran.store.controller;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart Management", description = "Operations related to shopping cart functionality")
public interface CartApi {

    @Operation(summary = "Add product to cart ", description = "Add a product with specified quantity to the user's cart")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cart item details", required = true,
            content = @Content(schema = @Schema(implementation = CartItemResponseDto.class)))
    @ApiResponse(responseCode = "201", description = "Successfully added product to cart",
            content = @Content(schema = @Schema(implementation = CartItemResponseDto.class)))
    CartItemResponseDto add(@Valid @RequestBody AddToCartRequestDto request);

    @Operation(summary = "Edit quantity of products to cart ", description = "Edit a quantity of products with specified quantity to the user's cart")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update cart item details", required = true,
            content = @Content(schema = @Schema(implementation = CartItemResponseDto.class)))
    @ApiResponse(responseCode = "201", description = "Successfully added product to cart",
            content = @Content(schema = @Schema(implementation = CartItemResponseDto.class)))
    CartResponseDto edit(@Valid @RequestBody AddToCartRequestDto cartRequest);

    @Operation(summary = "Get cart by user ID", description = "Get cart by user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved cart",
            content = @Content(schema = @Schema(implementation = CartResponseDto.class)))
    CartResponseDto getById();

    @Operation(summary = "Clear user's cart", description = "Removes all items from the authenticated user's cart")
    @ApiResponse(responseCode = "200", description = "Cart successfully cleared",
            content = @Content(schema = @Schema(implementation = CartResponseDto.class)))
    void clearCart();

    @Operation(summary = "Delete cart by ID (admin only)", description = "Deletes a cart by its ID. Accessible only by users with ADMIN role")
    @ApiResponse(responseCode = "200", description = "Cart successfully deleted",
            content = @Content(schema = @Schema(implementation = CartResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Access denied – only admins can perform this action")
    void deleteById();

    @Operation(summary = "Delete product from user's cart", description = "Removes a specific product from the authenticated user's cart")
    @Parameter(name = "product_id", description = "ID of the product to remove", required = true,
    schema = @Schema(type = "integer", format = "int64", example = "234"))
    @ApiResponse(responseCode = "200", description = "Product successfully removed from cart")
    @ApiResponse(responseCode = "404", description = "Product not found in cart")
    void deleteByUserId(@PathVariable("product_id") Long productId);
}
