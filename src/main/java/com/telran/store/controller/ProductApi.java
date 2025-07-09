package com.telran.store.controller;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Product Management", description = "Operations for managing products")
public interface ProductApi {

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new product (Only for admins)", description = "Return a new product.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product details", required = true,
            content = @Content(schema = @Schema(implementation = ProductCreateDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "name": "Shovel",
                              "description": "Big tool",
                              "price": 100.00,
                              "imageUrl": "https://example.com/images/shovel5.jpg",
                              "discountPrice": 80.00,
                              "category": {
                                "categoryId": 1,
                                "name": "Garden"
                              }
                            }
                            """)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": 1,
                              "name": "Shovel",
                              "description": "Big tool",
                              "price": 100.00,
                              "imageUrl": "https://example.com/images/shovel5.jpg",
                              "discountPrice": 80.00
                            }
                            """))),
            @ApiResponse(responseCode = "400", description = "Invalid product data",
            content = @Content(examples = @ExampleObject(value = "Invalid product data: name, description, price, image URL or category"))),
            @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role",
                    content = @Content(examples = @ExampleObject(value = "Forbidden - requires ADMIN role"))),
    })
    ProductResponseDto createProduct(@Valid @RequestBody ProductCreateDto dto);

    @GetMapping
    @Operation(summary = "Get all products", description = "Return all products with filtering")
    @Parameters({
            @Parameter(name = "category", description = "Category name", required = false, example = "Garden", schema = @Schema(type = "string")),
            @Parameter(name = "minPrice", description = "Minimal price", required = false, example = "10.00", schema = @Schema(type = "bigdecimal")),
            @Parameter(name = "maxPrice", description = "Maximal price", required = false, example = "2000.00", schema = @Schema(type = "bigdecimal")),
            @Parameter(name = "discount", description = "Discount value", required = false, example = "true", schema = @Schema(type = "boolean")),
            @Parameter(name = "sort", description = "Sort by id, price and name", required = false, schema = @Schema(type = "string", defaultValue = "id"))})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product filter or sort",
                    content = @Content(examples = @ExampleObject(value = "Invalid product filter or sort"))),
            @ApiResponse(responseCode = "404", description = "Products not found",
                    content = @Content(examples = @ExampleObject(value = "Product with filter parameters not found")))
    })
    List<ProductResponseDto> getAll(@RequestParam(name = "category", required = false) String category,
                                    @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                    @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                    @RequestParam(name = "discount", required = false) Boolean discount,
                                    @RequestParam(name = "sort", required = false, defaultValue = "id") String sort);

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Return the product by product ID.")
    @Parameter(name = "id", description = "ID of the product to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the product",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Incorrectly entered parameter",
                    content = @Content(examples = @ExampleObject(value = "Incorrectly entered parameter, correctly for product ID: 1"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(examples = @ExampleObject(value = "Product with ID 1 not found")))
    })
    ProductResponseDto getById(@PathVariable long id);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product by ID (Only for admins)", description = "Delete the product by their product ID.")
    @Parameter(name = "id", description = "ID of the product to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed the product"),
            @ApiResponse(responseCode = "400", description = "Invalid product ID"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    void deleteById(@PathVariable long id);

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Edit product (Only for admins)", description = "Update an existing product with new data")
    @Parameter(name = "id", description = "ID of the product to update", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product details", required = true,
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product updated successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data",
                    content = @Content(examples = @ExampleObject(value = "Incorrectly entered parameters"))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(examples = @ExampleObject(value = "Product with ID 1 not found")))
    })
    ProductResponseDto edit(@PathVariable Long id, @Valid @RequestBody ProductCreateDto dto);

    @GetMapping("/daily_product")
    @Operation(summary = "Get product of the day", description = "Retrieves the current product of the day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product of the day",
                    content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Product of the day not found",
            content = @Content(examples = @ExampleObject(value = "Daily product not found")))
    })
    ProductResponseDto getDailyProduct();
}
