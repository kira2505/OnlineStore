package com.telran.store.controller;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payment Management", description = "Operations for managing payment")
public interface PaymentApi {

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new payment", description = "Create new payment")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payment details", required = true,
            content = @Content(schema = @Schema(implementation = PaymentCreateDto.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The order was paid",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    PaymentResponseDto pay(@Valid @RequestBody PaymentCreateDto request);

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments (Only for admins)", description = "Get all payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Payments not found")
    })
    ResponseEntity<List<PaymentResponseDto>> getAllPayments();

    @GetMapping("{orderId}")
    @Operation(summary = "Get all payments by order ID", description = "Get all payments")
    @Parameter(name = "Order ID", description = "ID of the order to retrieves payments", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid order ID"),
            @ApiResponse(responseCode = "404", description = "Order or payments not found")
    })
    List<PaymentResponseDto> getPaymentsById(@PathVariable Long orderId);

    @GetMapping("/pending_orders")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders waiting more than this time (Only for admins)", description = "Get all orders waiting more than this time")
    @Parameter(name = "days", description = "Number of days the order is awaiting payment", required = true,
            schema = @Schema(type = "integer", defaultValue = "5"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders"),
            @ApiResponse(responseCode = "400", description = "Invalid date"),
            @ApiResponse(responseCode = "404", description = "Order or payments not found")
    })
    List<OrderPendingPaidDto> getOrdersWaitingMoreThan(@RequestParam int days);
}
