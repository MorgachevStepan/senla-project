package com.stepanew.senlaproject.controller;


import com.stepanew.senlaproject.controller.api.StoreApi;
import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import com.stepanew.senlaproject.services.StoreService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController implements StoreApi {

    private final StoreService storeService;

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.findById(id));
    }

    @GetMapping("/")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0L, message = "Page number can't be less than 0")
            Integer pageNumber,
            @RequestParam(defaultValue = "10")
            @Min(value = 1L, message = "Page limit can't be less than 1")
            Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String address,
            @RequestParam(required = false, defaultValue = "id")
            @Pattern(regexp = "(?i)id|name|address", message = "sortBy must be one of: id, name, address")
            String sortBy,
            @RequestParam(required = false, defaultValue = "ASC")
            @Pattern(regexp = "(?i)asc|desc", message = "sortDirection must be one of: asc, desc")
            String sortDirection
    ) {
        Page<StoreResponseDto> response = storeService.findAll(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.Direction.valueOf(sortDirection.toUpperCase()),
                        sortBy.toLowerCase()
                ),
                name,
                address
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createStore(@RequestBody @Validated StoreCreateRequestDto request) {
        StoreResponseDto response = storeService.create(request);
        return ResponseEntity
                .created(URI.create("/api/v1/store/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        storeService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStore(@RequestBody @Validated StoreUpdateRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.update(request));
    }

}
