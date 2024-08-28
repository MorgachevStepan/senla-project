package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.controller.api.CategoryApi;
import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryGetAllRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import com.stepanew.senlaproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Validated
@Slf4j
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController implements CategoryApi {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.findById(id));
    }

    @GetMapping("/")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getAll(@Validated CategoryGetAllRequestDto request) {
        Page<CategoryResponseDto> response = categoryService.findAll(
                PageRequest.of(
                        request.getPageNumber(),
                        request.getPageSize(),
                        Sort.Direction.valueOf(request.getSortDirection().toUpperCase()),
                        request.getSortBy().toLowerCase()
                ),
                request.getName()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody @Validated CategoryCreateRequestDto request) {
        CategoryResponseDto response = categoryService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/category/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCategory(@RequestBody @Validated CategoryUpdateRequestDto request) {
        return ResponseEntity.
                status(HttpStatus.OK)
                .body(categoryService.update(request));
    }

}
