package org.example.web_thue_gia_su_online.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.OrderUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.OrderResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private OrderCreationRequest request;
    private OrderUpdateRequest updateRequest;
    private OrderResponse response;
    private String studentId;
    private String tutorId;
    private String packageId;
    private String orderId;


    @BeforeEach
    void initData(){

        studentId = "11111111-1111-1111-1111-111111111111";
        tutorId = "22222222-2222-2222-2222-222222222222";
        packageId = "33333333-3333-3333-3333-333333333333";
        orderId = "99999999-999999999999-9999999999";

        request = OrderCreationRequest.builder()
                .total_price(1000)
                .sessions_remaining(10)
                .build();

        response = OrderResponse.builder()
                .id(orderId)
                .total_price(1000)
                .sessions_remaining(10)
                .build();
    }

    @Test
    void createOrder_success() throws Exception{
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(request);

        //WHEN
        Mockito.when(orderService.createOrder(
                ArgumentMatchers.any(OrderCreationRequest.class),
                Mockito.eq(studentId),
                Mockito.eq(tutorId),
                Mockito.eq(packageId)
        )).thenReturn(response);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/orders/{studentId}/{tutorId}/{packageId}",
                                studentId, tutorId, packageId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(orderId));

        Mockito.verify(orderService).createOrder(
                ArgumentMatchers.any(OrderCreationRequest.class),
                Mockito.eq(studentId),
                Mockito.eq(tutorId),
                Mockito.eq(packageId)
        );
    }

    @Test
    void getOrder_success() throws Exception{
        //GIVEN

        //WHEN
        Mockito.when(orderService.getOrder(Mockito.eq(orderId))).thenReturn(response);

        //THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value(orderId));
    }
}
