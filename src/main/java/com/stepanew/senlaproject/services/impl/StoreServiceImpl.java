package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import com.stepanew.senlaproject.domain.entity.Store;
import com.stepanew.senlaproject.domain.mapper.store.StoreCreateRequestDtoMapper;
import com.stepanew.senlaproject.domain.mapper.store.StoreResponseDtoMapper;
import com.stepanew.senlaproject.exceptions.StoreException;
import com.stepanew.senlaproject.repository.StoreRepository;
import com.stepanew.senlaproject.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final StoreResponseDtoMapper storeResponseDtoMapper;

    private final StoreCreateRequestDtoMapper storeCreateRequestDtoMapper;

    @Override
    public StoreResponseDto findById(Long id) {
        return storeResponseDtoMapper
                .toDto(findStoreById(id));
    }

    @Override
    @Transactional
    public StoreResponseDto create(StoreCreateRequestDto request) {
        Store store = storeCreateRequestDtoMapper.toEntity(request);

        storeRepository.save(store);

        return storeResponseDtoMapper.toDto(store);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        storeRepository.delete(
                findStoreById(id)
        );
    }

    @Override
    @Transactional
    public StoreResponseDto update(StoreUpdateRequestDto request) {
        Store updatedStore = findStoreById(request.id());

        if (request.name() != null) {
            updatedStore.setName(request.name());
        }
        if (request.address() != null) {
            updatedStore.setAddress(request.address());
        }
        if(request.number() != null) {
            updatedStore.setNumber(request.number());
        }

        storeRepository.save(updatedStore);

        return storeResponseDtoMapper.toDto(updatedStore);
    }

    @Override
    public Page<StoreResponseDto> findAll(Pageable pageable, String name, String address) {
        Page<Store> response = storeRepository
                .findAllByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(
                        name,
                        address,
                        pageable
                );

        return response.map(storeResponseDtoMapper::toDto);
    }

    private Store findStoreById(Long id) {
        return storeRepository
                .findById(id)
                .orElseThrow(StoreException.CODE.NO_SUCH_STORE::get);
    }

}
