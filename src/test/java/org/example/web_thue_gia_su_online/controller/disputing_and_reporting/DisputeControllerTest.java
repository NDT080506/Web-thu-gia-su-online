package org.example.web_thue_gia_su_online.controller.disputing_and_reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeCreationRequest;
import org.example.web_thue_gia_su_online.dto.request.disputing_and_reporting.DisputeUpdateRequest;
import org.example.web_thue_gia_su_online.dto.response.ApiResponse;
import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.DisputeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DisputeControllerTest {

    @Mock
    private DisputeService disputeService;

    @InjectMocks
    private DisputeController disputeController;

    @Test
    void createDispute_shouldReturnDisputeResponse() {
        DisputeCreationRequest request = DisputeCreationRequest.builder()
                .bookingId("booking-1")
                .studentId("student-1")
                .reason("Test reason")
                .build();
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(disputeService.createDispute(eq(request))).thenReturn(response);

        ApiResponse<DisputeResponse> apiResponse = disputeController.createDispute(request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(disputeService).createDispute(eq(request));
    }

    @Test
    void getDispute_shouldReturnDisputeResponse() {
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(disputeService.getDispute("1")).thenReturn(response);

        ApiResponse<DisputeResponse> apiResponse = disputeController.getDispute("1");

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(disputeService).getDispute("1");
    }

    @Test
    void getDisputes_shouldReturnListOfDisputes() {
        List<DisputeResponse> list = List.of(DisputeResponse.builder().id("1").build());
        when(disputeService.getDisputes()).thenReturn(list);

        ApiResponse<List<DisputeResponse>> apiResponse = disputeController.getDisputes();

        assertNotNull(apiResponse);
        assertEquals(1, apiResponse.getResult().size());
        verify(disputeService).getDisputes();
    }

    @Test
    void updateDispute_shouldReturnUpdatedDisputeResponse() {
        DisputeUpdateRequest request =
                DisputeUpdateRequest.builder().adminNote("Admin note").build();
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(disputeService.updateDispute(eq(request), eq("1"))).thenReturn(response);

        ApiResponse<DisputeResponse> apiResponse = disputeController.updateDispute("1", request);

        assertNotNull(apiResponse);
        assertEquals(1000, apiResponse.getCode());
        verify(disputeService).updateDispute(eq(request), eq("1"));
    }

    @Test
    void deleteDispute_shouldReturnSuccessMessage() {
        doNothing().when(disputeService).deleteDispute("1");

        ApiResponse<Void> apiResponse = disputeController.deleteDispute("1");

        assertNotNull(apiResponse);
        assertEquals("Dispute has been deleted", apiResponse.getMessage());
        verify(disputeService).deleteDispute("1");
    }
}
