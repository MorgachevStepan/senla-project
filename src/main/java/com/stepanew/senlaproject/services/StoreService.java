package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {

    StoreResponseDto findById(Long id);

    StoreResponseDto create(StoreCreateRequestDto request);

    void delete(Long id);

    StoreResponseDto update(StoreUpdateRequestDto request);

    Page<StoreResponseDto> findAll(Pageable pageable, String name, String address);

}
