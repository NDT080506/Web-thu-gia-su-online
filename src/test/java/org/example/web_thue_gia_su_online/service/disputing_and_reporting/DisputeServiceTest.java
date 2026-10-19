package org.example.web_thue_gia_su_online.service.disputing_and_reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.response.disputing_and_reporting.DisputeResponse;
import org.example.web_thue_gia_su_online.entity.disputing_and_reporting.Dispute;
import org.example.web_thue_gia_su_online.mapper.disputing_and_reporting.DisputeMapper;
import org.example.web_thue_gia_su_online.repository.disputing_and_reporting.DisputeRepository;
import org.example.web_thue_gia_su_online.repository.identity_and_administration.UserRepository;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BookingRepository;
import org.example.web_thue_gia_su_online.service.disputing_and_reporting.impl.DisputeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DisputeServiceTest {

    @Mock
    private DisputeRepository repository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DisputeMapper mapper;

    @InjectMocks
    private DisputeServiceImpl service;

    @Test
    void createDispute_shouldReturnResponse() {
        Dispute dispute = Dispute.builder().id("1").build();
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(bookingRepository.findById(any())).thenReturn(Optional.of(any()));
        when(userRepository.findById(any())).thenReturn(Optional.of(any()));
        when(repository.save(any())).thenReturn(dispute);
        when(mapper.toDisputeResponse(any())).thenReturn(response);

        DisputeResponse result = service.createDispute(any());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getDispute_shouldReturnResponse() {
        Dispute dispute = Dispute.builder().id("1").build();
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(dispute));
        when(mapper.toDisputeResponse(any())).thenReturn(response);

        DisputeResponse result = service.getDispute("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getDispute_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getDispute("1"));
    }

    @Test
    void getDisputes_shouldReturnList() {
        Dispute dispute = Dispute.builder().id("1").build();
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(dispute));
        when(mapper.toDisputeResponse(any())).thenReturn(response);

        List<DisputeResponse> result = service.getDisputes();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void updateDispute_shouldReturnResponse() {
        Dispute dispute = Dispute.builder().id("1").build();
        DisputeResponse response = DisputeResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(dispute));
        when(repository.save(any())).thenReturn(dispute);
        when(mapper.toDisputeResponse(any())).thenReturn(response);

        DisputeResponse result = service.updateDispute(any(), "1");

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void deleteDispute_shouldSucceed() {
        Dispute dispute = Dispute.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(dispute));
        doNothing().when(repository).delete(dispute);

        service.deleteDispute("1");

        verify(repository).delete(dispute);
    }
}
