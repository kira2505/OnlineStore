package com.telran.store.controller;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.dto.ReportRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Report", description = "Report operations (Only for admins)")
public interface ReportApi {

    @Operation(summary = "Get most cancelled products",
            description = "Returns a list of products with the highest cancellation count.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned cancelled products",
                    content = @Content(schema = @Schema(implementation = ProductSalesDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")})
    List<ProductSalesDTO> mostCancelled();

    @Operation(summary = "Get most purchased products",
            description = "Returns a list of top selling products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned purchased products",
                    content = @Content(schema = @Schema(implementation = ProductSalesDTO.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")})
    List<ProductSalesDTO> mostPurchased();

    @Operation(summary = "Get total profit",
            description = "Calculates total profit for a given period.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profit successfully calculated",
                    content = @Content(schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range or parameters"),})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Date range for report", required = true,
            content = @Content(schema = @Schema(implementation = ReportRequestDto.class)))
    BigDecimal getProfit(@Valid @RequestBody ReportRequestDto reportRequestDto);
}
