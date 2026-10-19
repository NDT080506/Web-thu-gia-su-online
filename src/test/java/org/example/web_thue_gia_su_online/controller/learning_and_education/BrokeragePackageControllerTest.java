package org.example.web_thue_gia_su_online.controller.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.service.learning_and_education.BrokeragePackageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrokeragePackageControllerTest {

    @Mock
    private BrokeragePackageService packageService;

    @InjectMocks
    private BrokeragePackageController controller;

    @Test
    void create_shouldReturnResponse() {
        BrokeragePackageCreationRequest request = BrokeragePackageCreationRequest.builder()
                .name("Basic Package")
                .price(100000L)
                .build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(packageService.create(eq(request))).thenReturn(response);
        ApiResponse<BrokeragePackageResponse> apiResponse = controller.create(request);
        assertNotNull(apiResponse);
        verify(packageService).create(eq(request));
    }

    @Test
    void getById_shouldReturnResponse() {
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(packageService.getById("1")).thenReturn(response);
        ApiResponse<BrokeragePackageResponse> apiResponse = controller.getById("1");
        assertNotNull(apiResponse);
        verify(packageService).getById("1");
    }

    @Test
    void getAll_shouldReturnList() {
        List<BrokeragePackageResponse> list =
                List.of(BrokeragePackageResponse.builder().id("1").build());
        when(packageService.getAll()).thenReturn(list);
        ApiResponse<List<BrokeragePackageResponse>> apiResponse = controller.getAll();
        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
    }

    @Test
    void adminUpdate_shouldReturnResponse() {
        BrokeragePackageUpdateRequest request =
                BrokeragePackageUpdateRequest.builder().name("Updated Package").build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(packageService.update(eq("1"), eq(request))).thenReturn(response);
        ApiResponse<BrokeragePackageResponse> apiResponse = controller.adminUpdate("1", request);
        assertNotNull(apiResponse);
        verify(packageService).update(eq("1"), eq(request));
    }

    @Test
    void tutorUpdate_shouldReturnResponse() {
        BrokeragePackageUpdateRequest request =
                BrokeragePackageUpdateRequest.builder().name("Tutor Updated").build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(packageService.update(eq("1"), eq(request))).thenReturn(response);
        ApiResponse<BrokeragePackageResponse> apiResponse = controller.tutorUpdate("1", request);
        assertNotNull(apiResponse);
        verify(packageService).update(eq("1"), eq(request));
    }

    @Test
    void delete_shouldReturnSuccessMessage() {
        doNothing().when(packageService).delete("1");
        ApiResponse<Void> apiResponse = controller.delete("1");
        assertNotNull(apiResponse);
        assertEquals("Brokerage package deleted successfully", apiResponse.getMessage());
    }
}
