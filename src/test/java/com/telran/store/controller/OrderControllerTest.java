package com.telran.store.controller;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.entity.Order;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import com.telran.store.mapper.OrderMapper;
import com.telran.store.service.OrderService;
import com.telran.store.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper orderMapper;

    @Test
    void testCreateOrder() throws Exception {

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(1L);
        responseDto.setDeliveryAddress("address");
        responseDto.setDeliveryMethod("courier");

        when(orderService.createOrder(any(OrderCreateDto.class))).thenReturn(new Order());
        when(orderMapper.toDto(any(Order.class))).thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deliveryAddress\": \"address\", \"deliveryMethod\": \"courier\", \"orderItems\": [{\"productId\": 2, \"quantity\": 3}] }"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryAddress").value("address"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryMethod").value("courier"));
    }

    @Test
    void testGetOrderById() throws Exception {
        Long orderId = 1L;

        Order order = Order.builder().id(orderId).build();

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(orderId);
        orderResponseDto.setDeliveryAddress("address");

        when(orderService.getById(orderId)).thenReturn(order);
        when(orderMapper.toDto(any())).thenReturn(orderResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryAddress").value("address"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<Order> orders = List.of(new Order(), new Order());
        OrderResponseDto dto1 = new OrderResponseDto();
        dto1.setId(1L);
        dto1.setDeliveryAddress("Address 1");
        dto1.setDeliveryMethod("Courier");

        OrderResponseDto dto2 = new OrderResponseDto();
        dto2.setId(2L);
        dto2.setDeliveryAddress("Address 2");
        dto2.setDeliveryMethod("Courier");

        List<OrderResponseDto> dtos = List.of(dto1, dto2);


        when(orderService.getAllOrdersCurrentUser()).thenReturn(orders);
        when(orderMapper.toDtoList(orders)).thenReturn(dtos);

        mockMvc.perform(get("/orders/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testCancelOrder() throws Exception {
        Long orderId = 1L;
        Order order = Order.builder().id(orderId).paymentStatus(PaymentStatus.CANCELED).status(Status.CANCELED).build();

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(orderId);
        responseDto.setStatus(Status.CANCELED);
        responseDto.setPaymentStatus(PaymentStatus.CANCELED);

        when(orderService.cancelOrder(orderId)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(responseDto);

        mockMvc.perform(patch("/orders/{orderId}/close", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("CANCELED"))
                .andExpect(jsonPath("$.paymentStatus").value("CANCELED"));

        verify(orderService).cancelOrder(orderId);
        verify(orderMapper).toDto(order);
    }
}