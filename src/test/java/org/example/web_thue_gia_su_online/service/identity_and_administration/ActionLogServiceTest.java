package org.example.web_thue_gia_su_online.service.identity_and_administration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.identity_and_administration.ActionLogCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.identity_and_administration.ActionLogResponse;
import org.example.web_thue_gia_su_online.entity.identity_and_administration.ActionLog;
import org.example.web_thue_gia_su_online.mapper.identity_and_administration.ActionLogMapper;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.ActionLogRepository;
import org.example.web_thue_gia_su_online.service.identity_and_administration.impl.ActionLogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActionLogServiceTest {

    @Mock
    private ActionLogRepository repository;

    @Mock
    private ActionLogMapper mapper;

    @InjectMocks
    private ActionLogServiceImpl service;

    @Test
    void logAction_shouldReturnResponse() {
        ActionLogCreationRequest request = ActionLogCreationRequest.builder()
                .adminId("admin-1")
                .targetUserId("user-1")
                .actionCode("BAN")
                .reason("Violation")
                .build();

        ActionLog log = ActionLog.builder().id("1").build();
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(mapper.toEntity(request)).thenReturn(log);
        when(repository.save(any())).thenReturn(log);
        when(mapper.toResponse(any())).thenReturn(response);

        ActionLogResponse result = service.logAction(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getLog_shouldReturnResponse() {
        ActionLog log = ActionLog.builder().id("1").build();
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(log));
        when(mapper.toResponse(log)).thenReturn(response);

        ActionLogResponse result = service.getLog("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getLog_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getLog("1"));
    }

    @Test
    void getLogsByTargetUser_shouldReturnList() {
        ActionLog log = ActionLog.builder().id("1").build();
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(repository.findByTargetUserId("user-1")).thenReturn(List.of(log));
        when(mapper.toResponse(log)).thenReturn(response);

        List<ActionLogResponse> result = service.getLogsByTargetUser("user-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getLogsByAdmin_shouldReturnList() {
        ActionLog log = ActionLog.builder().id("1").build();
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(repository.findByAdminId("admin-1")).thenReturn(List.of(log));
        when(mapper.toResponse(log)).thenReturn(response);

        List<ActionLogResponse> result = service.getLogsByAdmin("admin-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAllLogs_shouldReturnList() {
        ActionLog log = ActionLog.builder().id("1").build();
        ActionLogResponse response = ActionLogResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(log));
        when(mapper.toResponse(log)).thenReturn(response);

        List<ActionLogResponse> result = service.getAllLogs();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
