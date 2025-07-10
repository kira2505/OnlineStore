package com.telran.store.controller;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            content = @Content(schema = @Schema(implementation = PaymentCreateDto.class),
                    examples = @ExampleObject(value = "{ \"orderId\": 1, \"amount\": 49.99}")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The order was paid",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment data",
                    content = @Content(examples = @ExampleObject(value = "Amount must be greater than 0"))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(examples = @ExampleObject(value = "Order with ID 1 not found")))
    })
    PaymentResponseDto pay(@Valid @RequestBody PaymentCreateDto request);

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all payments (Only for admins)", description = "Get all payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Payments not found",
                    content = @Content(examples = @ExampleObject(value = "No payment records found")))
    })
    ResponseEntity<List<PaymentResponseDto>> getAllPayments();

    @GetMapping("{orderId}")
    @Operation(summary = "Get all payments by order ID", description = "Get all payments")
    @Parameter(name = "Order ID", description = "ID of the order to retrieves payments", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
                    content = @Content(schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid order ID",
                    content = @Content(examples = @ExampleObject(value = "Order ID must be a positive integer"))),
            @ApiResponse(responseCode = "404", description = "Order or payments not found",
                    content = @Content(examples = @ExampleObject(value = "No payments found for order ID 1")))
    })
    List<PaymentResponseDto> getPaymentsById(@PathVariable Long orderId);

    @GetMapping("/pending_orders")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders waiting more than this time (Only for admins)",
            description = "Retrieves a list of orders that are waiting for payment longer than the specified number of days")
    @Parameter(name = "days", description = "Number of days the order is awaiting payment", required = true,
            schema = @Schema(type = "integer", defaultValue = "5"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                    content = @Content(schema = @Schema(implementation = OrderPendingPaidDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date",
                    content = @Content(examples = @ExampleObject(value = "Days must be a non-negative number"))),
            @ApiResponse(responseCode = "404", description = "Order or payments not found",
                    content = @Content(examples = @ExampleObject(value = "No pending orders found older than 5 days")))
    })
    List<OrderPendingPaidDto> getOrdersWaitingMoreThan(@RequestParam int days);
}
