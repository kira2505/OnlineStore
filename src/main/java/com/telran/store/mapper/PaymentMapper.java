package com.telran.store.mapper;

import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "dateTime", target = "paymentDate")
    PaymentResponseDto toDto(Payment payment);

    Payment toEntity(PaymentCreateDto paymentCreateDto);

    List<PaymentResponseDto> toDtoList(List<Payment> payments);
}
