package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface StoreService {

    @Transactional(readOnly = true)
    StoreResponseDto findById(Long id);

    @Transactional(readOnly = false)
    StoreResponseDto create(StoreCreateRequestDto request);

    @Transactional(readOnly = false)
    void delete(Long id);

    @Transactional(readOnly = false)
    StoreResponseDto update(StoreUpdateRequestDto request);

    @Transactional(readOnly = true)
    Page<StoreResponseDto> findAll(Pageable pageable, String name, String address);

}
