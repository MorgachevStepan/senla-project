package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.request.CategoryCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.CategoryUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.CategoryResponseDto;
import com.stepanew.senlaproject.services.CategoryService;
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

@RestController
@Validated
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

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
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0L, message = "Page number can't be less than 0")
            Integer pageNumber,
            @RequestParam(defaultValue = "10")
            @Min(value = 1L, message = "Page limit can't be less than 1")
            Integer pageSize,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "id")
            @Pattern(regexp = "(?i)id|name", message = "sortBy must be one of: id, name")
            String sortBy,
            @RequestParam(required = false, defaultValue = "ASC")
            @Pattern(regexp = "(?i)asc|desc", message = "sortDirection must be one of: asc, desc")
            String sortDirection
    ) {
        Page<CategoryResponseDto> response = categoryService.findAll(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.Direction.valueOf(sortDirection.toUpperCase()),
                        sortBy.toLowerCase()
                ),
                name
        );

        if (response.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCategory(@RequestBody @Validated CategoryCreateRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryService.create(request));
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
