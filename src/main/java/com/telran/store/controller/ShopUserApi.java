package com.telran.store.controller;

import com.telran.store.dto.LoginRequestDto;
import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserDto;
import com.telran.store.dto.ShopUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "User", description = "Custom operations")
public interface ShopUserApi {

    @Operation(summary = "Create user",
            description = "Returns a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created",
                    content = @Content(schema = @Schema(implementation = ShopUserResponseDto.class),
                            examples = {
                                    @ExampleObject(value = """
                                            {
                                              "id": 1,
                                              "name": "John",
                                              "email": "john@example.com",
                                              "phoneNumber": "+123456789012",
                                              "role": "ROLE_USER"
                                            }
                                            """
                                    )})),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"Field: must not be null\"")
                    ))})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Required fields for creating a new user",
            required = true,
            content = @Content(schema = @Schema(implementation = ShopUserCreateDto.class),
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "name": "John",
                                      "email": "john@example.com",
                                      "phoneNumber": "+123456789012",
                                      "password": "StrongPassword78&8*"
                                    }
                                    """
                            )}))
    ShopUserResponseDto create(@Valid @RequestBody ShopUserCreateDto shopUserCreateDto);

    @Operation(summary = "User login",
            description = "Authenticates the user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9\"")
                    )),
            @ApiResponse(responseCode = "401", description = "Invalid login or password",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"Invalid username or password\"")
                    ))})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            required = true,
            content = @Content(schema = @Schema(implementation = LoginRequestDto.class),
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "login": "john@example.com",
                                      "password": "StrongPassword78&8*"
                                    }
                                    """
                            )}))
    String login(@RequestBody LoginRequestDto loginRequestDto);

    @Operation(summary = "Get all users (Only for admins)",
            description = "Returns all users.")
    @ApiResponse(responseCode = "200", description = "List of users returned successfully",
            content = @Content(schema = @Schema(implementation = ShopUserResponseDto.class),
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "John",
                                      "email": "john@example.com",
                                      "phoneNumber": "+123456789012",
                                      "role": "ROLE_USER"
                                    },
                                    {
                                      "id": 2,
                                      "name": "Max",
                                      "email": "max@example.com",
                                      "phoneNumber": "+124456789912",
                                      "role": "ROLE_ADMIN"
                                    }
                                    """
                            )}))
    List<ShopUserResponseDto> getAll();

    @Operation(summary = "Get user by ID (Only for admins)",
            description = "Returns the user by their user ID.")
    @Parameter(name = "id", description = "Unique user identifier.", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found and returned",
                    content = @Content(examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "John",
                                      "email": "john@example.com",
                                      "phoneNumber": "+123456789012",
                                      "role": "ROLE_USER"
                                    }
                                    """
                            )})),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"User with ID 99 not found\"")
                    ))})
    ShopUserResponseDto getById(@PathVariable long id);

    @Operation(summary = "Delete user by ID (Only for admins)",
            description = "Delete the user by their user ID.")
    @Parameter(name = "id", description = "Unique user identifier.", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    void deleteById(@PathVariable long id);

    @Operation(summary = "Update curren user",
            description = "Returns the updated current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated",
                    content = @Content(schema = @Schema(implementation = ShopUserResponseDto.class),
                            examples = {
                                    @ExampleObject(value = """
                                            {
                                              "id": 1,
                                              "name": "John",
                                              "email": "john@example.com",
                                              "phoneNumber": "+123456789012",
                                              "role": "ROLE_USER"
                                            }
                                            """
                                    )})),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "array", example = """
                            [
                              "name: must not be blank",
                              "phoneNumber: invalid format"
                            ]
                            """)
                    )),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"User with ID 99 not found\"")
                    ))})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Fields that can be changed", required = true,
            content = @Content(schema = @Schema(implementation = ShopUserDto.class),
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "name": "John",
                                      "phoneNumber": "+123456789012"
                                    }
                                    """
                            )}))
    ShopUserResponseDto edit(@Valid @RequestBody ShopUserDto shopUserDto);

    @Operation(summary = "Assign admin status to a user by ID (Only for admins)",
            description = "Returns the user.")
    @Parameter(name = "id", description = "Unique user identifier.", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin status assigned successfully",
                    content = @Content(examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "John",
                                      "email": "john@example.com",
                                      "phoneNumber": "+123456789012",
                                      "role": "ROLE_ADMIN"
                                    }
                                    """
                            )})),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "\"User with ID 99 not found\"")
                    ))})
    ShopUserResponseDto assignAdminStatus(@PathVariable Long id);
}
