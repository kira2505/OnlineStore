package com.telran.store.controller;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.dto.ReportRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Report Management", description = "Generate sales and profit reports. (Admins only)")
public interface ReportApi {

    @Operation(summary = "Get most cancelled products",
            description = "Returns a list of products with the highest cancellation count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned cancelled products",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductSalesDTO.class)),
                            examples = @ExampleObject(value = """
                                        [
                                          {
                                            "productId": 2,
                                            "productName": "Swimming pool",
                                            "totalQuantity": 15
                                          },
                                          {
                                            "productId": 3,
                                            "productName": "Generator",
                                            "totalQuantity": 10
                                          }
                                        ]
                                    """))),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                        {
                                          "timestamp": "2025-07-08T14:22:00.123+00:00",
                                          "status": 403,
                                          "error": "Forbidden",
                                          "message": "Access is denied",
                                          "path": "/products/most-cancelled"
                                        }
                                    """)))})
    @GetMapping("/most-cancelled")
    @PreAuthorize("hasRole('ADMIN')")
    List<ProductSalesDTO> mostCancelled();

    @Operation(summary = "Get most purchased products",
            description = "Returns a list of top selling products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned purchased products",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductSalesDTO.class)),
                            examples = @ExampleObject(value = """
                                        [
                                          {
                                            "productId": 2,
                                            "productName": "Swimming pool",
                                            "totalQuantity": 15
                                          },
                                          {
                                            "productId": 3,
                                            "productName": "Generator",
                                            "totalQuantity": 10
                                          }
                                        ]
                                    """))),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                        {
                                          "timestamp": "2025-07-08T14:22:00.123+00:00",
                                          "status": 403,
                                          "error": "Forbidden",
                                          "message": "Access is denied",
                                          "path": "/products/top-sellers"
                                        }
                                    """)))})
    @GetMapping("/top-sellers")
    @PreAuthorize("hasRole('ADMIN')")
    List<ProductSalesDTO> mostPurchased();

    @Operation(summary = "Get total profit",
            description = "Calculates total profit for a given period.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profit successfully calculated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class),
                            examples = @ExampleObject(value = "12500.75")
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid date range or parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"Invalid date unit. Must be one of: hours, minutes, days, month, years\"")
                    ))})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Date range for report", required = true,
            content = @Content(schema = @Schema(implementation = ReportRequestDto.class)))
    @PostMapping("/profit")
    @PreAuthorize("hasRole('ADMIN')")
    BigDecimal getProfit(@Valid @RequestBody ReportRequestDto reportRequestDto);
}
