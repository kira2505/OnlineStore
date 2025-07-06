package com.telran.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPendingPaidDto {

    private Long orderId;

    private LocalDateTime createdDate;

    private long dayWaiting;
}
