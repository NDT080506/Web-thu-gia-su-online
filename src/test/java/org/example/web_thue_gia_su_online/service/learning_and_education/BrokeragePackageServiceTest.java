package org.example.web_thue_gia_su_online.service.learning_and_education;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.web_thue_gia_su_online.dto.request.learning_and_education.BrokeragePackageCreationRequest;
import org.example.web_thue_gia_su_online.dto.response.learning_and_education.BrokeragePackageResponse;
import org.example.web_thue_gia_su_online.entity.learning_and_education.BrokeragePackage;
import org.example.web_thue_gia_su_online.mapper.learning_and_education.BrokeragePackageMapper;
import org.example.web_thue_gia_su_online.repository.learning_and_education.BrokeragePackageRepository;
import org.example.web_thue_gia_su_online.service.learning_and_education.impl.BrokeragePackageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrokeragePackageServiceTest {

    @Mock
    private BrokeragePackageRepository repository;

    @Mock
    private BrokeragePackageMapper mapper;

    @InjectMocks
    private BrokeragePackageServiceImpl service;

    @Test
    void create_shouldReturnResponse() {
        BrokeragePackageCreationRequest request = BrokeragePackageCreationRequest.builder()
                .name("Basic Package")
                .price(100000L)
                .build();

        BrokeragePackage pkg = BrokeragePackage.builder().id("1").build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(mapper.toEntity(request)).thenReturn(pkg);
        when(repository.save(any())).thenReturn(pkg);
        when(mapper.toResponse(any())).thenReturn(response);

        BrokeragePackageResponse result = service.create(request);

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void getById_shouldReturnResponse() {
        BrokeragePackage pkg = BrokeragePackage.builder().id("1").build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(pkg));
        when(mapper.toResponse(pkg)).thenReturn(response);

        BrokeragePackageResponse result = service.getById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getById_notFound_shouldThrow() {
        when(repository.findById("1")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById("1"));
    }

    @Test
    void getAll_shouldReturnList() {
        BrokeragePackage pkg = BrokeragePackage.builder().id("1").build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(repository.findAll()).thenReturn(List.of(pkg));
        when(mapper.toResponse(pkg)).thenReturn(response);

        List<BrokeragePackageResponse> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void update_shouldReturnResponse() {
        BrokeragePackage pkg = BrokeragePackage.builder().id("1").build();
        BrokeragePackageResponse response =
                BrokeragePackageResponse.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(pkg));
        when(repository.save(any())).thenReturn(pkg);
        when(mapper.toResponse(any())).thenReturn(response);

        BrokeragePackageResponse result = service.update("1", any());

        assertNotNull(result);
        verify(repository).save(any());
    }

    @Test
    void delete_shouldSucceed() {
        BrokeragePackage pkg = BrokeragePackage.builder().id("1").build();
        when(repository.findById("1")).thenReturn(Optional.of(pkg));

        service.delete("1");

        verify(repository).delete(pkg);
    }
}
