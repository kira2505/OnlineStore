package com.telran.store.controller;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Tag(name = "Order Management", description = "Manage and track customer orders.")
public interface OrderApi {

    @Operation(summary = "Create a new order",
            description = "Creates a new customer order based on cart contents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "contactPhone": "+12025550188",
                                      "deliveryAddress": "42 Gardeners Lane, Greenfield",
                                      "deliveryMethod": "Courier",
                                      "createdAt": "2025-07-08T15:10:00",
                                      "updatedAt": "2025-07-08T15:10:00",
                                      "status": "NEW",
                                      "orderItems": [
                                        {
                                          "id": 2,
                                          "productName": "Garden Rake",
                                          "quantity": 1,
                                          "priceAtPurchase": 108.99
                                        }
                                      ],
                                      "totalPrice": 108.99,
                                      "paymentAmount": 108.99,
                                      "paymentStatus": "PENDING_PAID"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "array", example = """
                            [
                              "Shipping address must not be empty",
                              "The Order Items list must not be empty",
                              "Shipping method must not be empty"
                            ]
                            """)
            ))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order data", required = true,
            content = @Content(schema = @Schema(implementation = OrderCreateDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "orderItems": [
                                {
                                  "quantity": 1,
                                  "productId": 2
                                }
                              ],
                              "deliveryAddress": "42 Gardeners Lane, Greenfield",
                              "deliveryMethod": "Courier"
                            }
                            """)))
    @PostMapping
    OrderResponseDto createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto);

    @Operation(summary = "Get order by ID",
            description = "Returns a specific order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "contactPhone": "+12025550188",
                                      "deliveryAddress": "42 Gardeners Lane, Greenfield",
                                      "deliveryMethod": "Courier",
                                      "createdAt": "2025-07-08T15:10:00",
                                      "updatedAt": "2025-07-08T15:10:00",
                                      "status": "NEW",
                                      "orderItems": [
                                        {
                                          "id": 2,
                                          "productName": "Garden Rake",
                                          "quantity": 1,
                                          "priceAtPurchase": 108.99
                                        }
                                      ],
                                      "totalPrice": 108.99,
                                      "paymentAmount": 108.99,
                                      "paymentStatus": "PENDING_PAID"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(examples = @ExampleObject(value = "Order with ID 1 not found")))})
    @Parameter(name = "orderId", description = "Unique identifier of the order", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @GetMapping("{orderId}")
    OrderResponseDto getById(@PathVariable Long orderId);

    @Operation(summary = "Get current user's order history",
            description = "Returns the list of orders made by the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of orders returned",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "id": 1,
                                        "contactPhone": "+1-202-555-0188",
                                        "deliveryAddress": "42 Gardeners Lane, Greenfield",
                                        "deliveryMethod": "Courier",
                                        "createdAt": "2025-07-08T15:10:00",
                                        "updatedAt": "2025-07-08T15:10:00",
                                        "status": "DELIVERED",
                                        "orderItems": [
                                          {
                                            "productId": 3,
                                            "productName": "Watering Can",
                                            "quantity": 1,
                                            "price": 12.50
                                          }
                                        ],
                                        "totalPrice": 12.50,
                                        "paymentAmount": 12.50,
                                        "paymentStatus": "PAID"
                                      },
                                      {
                                        "id": 2,
                                        "contactPhone": "+1-202-555-0188",
                                        "deliveryAddress": "42 Gardeners Lane, Greenfield",
                                        "deliveryMethod": "Courier",
                                        "createdAt": "2025-07-06T10:00:00",
                                        "updatedAt": "2025-07-06T10:00:00",
                                        "status": "NEW",
                                        "orderItems": [
                                          {
                                            "productId": 4,
                                            "productName": "Garden Gloves",
                                            "quantity": 2,
                                            "price": 7.99
                                          }
                                        ],
                                        "totalPrice": 15.98,
                                        "paymentAmount": 15.98,
                                        "paymentStatus": "PENDING_PAID"
                                      }
                                    ]
                                    """))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - JWT token is missing or invalid",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-07-08T12:00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "JWT token is missing or invalid",
                                      "path": "/orders/history"
                                    }
                                    """)))})
    @GetMapping("/history")
    List<OrderResponseDto> getAllOrders();

    @Operation(summary = "Close (cancel) an order",
            description = "Cancels the order by its ID. Only allowed if order is in a cancellable state.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully cancelled",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(name = "Order Cancelled Example", value = """
                                {
                                  "id": 1,
                                  "contactPhone": "+1-202-555-0188",
                                  "deliveryAddress": "42 Gardeners Lane, Greenfield",
                                  "deliveryMethod": "Courier",
                                  "createdAt": "2025-07-06T10:00:00",
                                  "updatedAt": "2025-07-08T16:00:00",
                                  "status": "CANCELLED",
                                  "orderItems": [
                                    {
                                      "productId": 4,
                                      "productName": "Garden Gloves",
                                      "quantity": 2,
                                      "price": 7.99
                                    }
                                  ],
                                  "totalPrice": 15.98,
                                  "paymentAmount": 15.98,
                                  "paymentStatus": "REFUNDED"
                                }
                                """))),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled",
                    content = @Content(examples = @ExampleObject(value = "Order is already completed and cannot be canceled"))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(examples = @ExampleObject(value = "Order with ID 1 not found")))})
    @Parameter(name = "orderId", description = "Unique identifier of the order", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @PatchMapping("/{orderId}/close")
    OrderResponseDto closeOrder(@PathVariable Long orderId);
}
