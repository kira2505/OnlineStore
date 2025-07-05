package com.telran.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {

    @NotNull(message = "All fields must be filled in")
    private long amount;

    @NotBlank(message = "All fields must be filled in")
    @Pattern(regexp = "^(minutes|hours|days|month|years)$",
            message = "Invalid date unit. Must be one of: hours, minutes, days, month, years")
    private String value;
}
