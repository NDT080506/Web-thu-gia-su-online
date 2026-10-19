package org.example.web_thue_gia_su_online.service.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.MessageCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.MessageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.Message;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.MessageMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.MessageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository repository;

    @Mock
    private MessageMapper mapper;

    @InjectMocks
    private MessageServiceImpl service;

    @Test
    void create_shouldReturnResponse() {
        MessageCreationRequest request = MessageCreationRequest.builder()
                .bookingId("booking-1")
                .senderId("sender-1")
                .content("Hello")
                .build();

        Message message = Message.builder().id(1L).build();
        MessageResponse response = MessageResponse.builder().id(1L).build();
        when(mapper.toEntity(request)).thenReturn(message);
        when(repository.save(any())).thenReturn(message);
        when(mapper.toResponse(any())).thenReturn(response);

        MessageResponse result = service.create(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getByBookingId_shouldReturnList() {
        Message message = Message.builder().id(1L).build();
        MessageResponse response = MessageResponse.builder().id(1L).build();
        when(repository.findByBookingIdOrderByCreatedAtAsc("booking-1")).thenReturn(List.of(message));
        when(mapper.toResponse(message)).thenReturn(response);

        List<MessageResponse> result = service.getByBookingId("booking-1");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void update_shouldReturnResponse() {
        Message message = Message.builder().id(1L).build();
        MessageResponse response = MessageResponse.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(message));
        when(repository.save(any())).thenReturn(message);
        when(mapper.toResponse(any())).thenReturn(response);

        MessageResponse result = service.update(1L, any());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void update_notFound_shouldThrow() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.update(1L, any()));
    }

    @Test
    void delete_shouldSucceed() {
        Message message = Message.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(message));

        service.delete(1L);

        verify(repository).delete(message);
    }
}
