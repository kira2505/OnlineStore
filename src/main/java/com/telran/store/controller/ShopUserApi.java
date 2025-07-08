package com.telran.store.controller;

import com.telran.store.dto.ShopUserCreateDto;
import com.telran.store.dto.ShopUserDto;
import com.telran.store.dto.ShopUserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "User", description = "Custom operations")
public interface ShopUserApi {

    @Operation(summary = "Create user", description = "Returns a new user.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Created user", required = true,
            content = @Content(schema = @Schema(implementation = ShopUserCreateDto.class)))
    ShopUserResponseDto create(@Valid @RequestBody ShopUserCreateDto shopUserCreateDto);

    @Operation(summary = "Get all users (Only for admins)", description = "Returns all users.")
    List<ShopUserResponseDto> getAll();

    @Operation(summary = "Get user by id (Only for admins)", description = "Returns the user by their user ID.")
    @Parameter(name = "id", description = "Unique user identifier.", required = true,
    schema =  @Schema(type = "integer", format = "int64", example = "1"))
    ShopUserResponseDto getById(@PathVariable long id);

    @Operation(summary = "Delete user by id (Only for admins)", description = "Delete the user by their user ID.")
    @Parameter(name = "id", description = "Unique user identifier.", required = true,
            schema =  @Schema(type = "integer", format = "int64", example = "1"))
    void deleteById(@PathVariable long id);

    @Operation(summary = "Update curren user", description = "Returns the updated current user.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Any from two fields", required = true,
            content = @Content(schema = @Schema(implementation = ShopUserDto.class)))
    ShopUserResponseDto edit(@Valid @RequestBody ShopUserDto shopUserDto);

    @Operation(summary = "Assign admin status to a user (Only for admins)", description = "Returns the user.")
    @Parameter(name = "id", description = "Unique user identifier.", required = true,
            schema =  @Schema(type = "integer", format = "int64", example = "1"))
    ShopUserResponseDto assignAdminStatus(@PathVariable Long id);
}
