package com.stepanew.senlaproject.service;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import com.stepanew.senlaproject.domain.entity.Store;
import com.stepanew.senlaproject.domain.mapper.store.StoreCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.store.StoreResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.StoreException;
import com.stepanew.senlaproject.repository.StoreRepository;
import com.stepanew.senlaproject.services.impl.StoreServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreResponseDtoMapper storeResponseDtoMapper;

    @Mock
    private StoreCreateRequestDtoMapper storeCreateRequestDtoMapper;

    @InjectMocks
    private StoreServiceImpl storeService;

    private final static Long DEFAULT_ID = 1L;

    private final static String DEFAULT_NAME = "NAME";

    private final static String DEFAULT_ADDRESS = "ADDRESS";

    private final static String DEFAULT_NUMBER = "88005553535";

    @Test
    void findById() {
        Store store = createDefaultStore();
        StoreResponseDto expectedResponse = createDefaultStoreResponseDto();

        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(store));
        when(storeResponseDtoMapper.toDto(store))
                .thenReturn(expectedResponse);

        StoreResponseDto testStore = storeService.findById(DEFAULT_ID);

        verify(storeRepository).findById(DEFAULT_ID);
        verify(storeResponseDtoMapper).toDto(store);
        Assertions.assertEquals(expectedResponse, testStore);
    }

    @Test
    void findByIdThrowsException() {
        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        assertThrowsStoreException(() -> storeService.findById(DEFAULT_ID));
    }

    @Test
    void create() {
        StoreCreateRequestDto request = createStoreCreateRequestDto();
        Store store = createDefaultStore();
        StoreResponseDto expectedResponse = createDefaultStoreResponseDto();

        when(storeCreateRequestDtoMapper.toEntity(request))
                .thenReturn(store);
        when(storeRepository.save(store))
                .thenReturn(store);
        when(storeResponseDtoMapper.toDto(store))
                .thenReturn(expectedResponse);

        StoreResponseDto result = storeService.create(request);

        Assertions.assertEquals(expectedResponse, result);
    }

    @Test
    void delete() {
        Store store = createDefaultStore();

        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(store));

        storeService.delete(DEFAULT_ID);

        verify(storeRepository).delete(store);
    }

    @Test
    void deleteThrowsException() {
        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        assertThrowsStoreException(() -> storeService.delete(DEFAULT_ID));
    }

    @Test
    void update() {
        StoreUpdateRequestDto request = createStoreUpdateRequestDto();
        Store store = createDefaultStore();
        StoreResponseDto expectedResponse = createDefaultStoreResponseDto();

        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.of(store));
        when(storeRepository.save(store))
                .thenReturn(store);
        when(storeResponseDtoMapper.toDto(store))
                .thenReturn(expectedResponse);

        StoreResponseDto result = storeService.update(request);

        Assertions.assertEquals(expectedResponse, result);
    }

    @Test
    void updateThrowsExceptionWhenStoreNotFound() {
        StoreUpdateRequestDto request = createStoreUpdateRequestDto();

        when(storeRepository.findById(DEFAULT_ID))
                .thenReturn(Optional.empty());

        assertThrowsStoreException(() -> storeService.update(request));
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Store store = createDefaultStore();
        Page<Store> storesPage = new PageImpl<>(Collections.singletonList(store));
        StoreResponseDto expectedResponse = createDefaultStoreResponseDto();

        when(storeRepository
                .findAllByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
                        "", "", pageable))
                .thenReturn(storesPage);
        when(storeResponseDtoMapper.toDto(store))
                .thenReturn(expectedResponse);

        Page<StoreResponseDto> result = storeService.findAll(pageable, "", "");

        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(expectedResponse.id(), result.getContent().get(0).id());
    }

    private void assertThrowsStoreException(Runnable action) {
        Assertions.assertThrows(
                StoreException.class, action::run
        );
    }

    private StoreUpdateRequestDto createStoreUpdateRequestDto() {
        return new StoreUpdateRequestDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_ADDRESS,
                DEFAULT_NUMBER
        );
    }

    private StoreCreateRequestDto createStoreCreateRequestDto() {
        return new StoreCreateRequestDto(
                DEFAULT_NAME,
                DEFAULT_ADDRESS,
                DEFAULT_NUMBER
        );
    }

    private Store createDefaultStore() {
        return new Store(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_ADDRESS,
                DEFAULT_NUMBER,
                null
        );
    }

    private StoreResponseDto createDefaultStoreResponseDto() {
        return new StoreResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_ADDRESS,
                DEFAULT_NUMBER
        );
    }
}
