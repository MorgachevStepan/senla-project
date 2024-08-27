package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.controller.api.ProductApi;
import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.ProductBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
import com.stepanew.senlaproject.services.ProductService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductApi {

    private final ProductService productService;

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.findById(id));
    }

    @GetMapping("/")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getAllByCategory(
            @RequestParam(defaultValue = "0")
            @Min(value = 0L, message = "Page number can't be less than 0")
            Integer pageNumber,
            @RequestParam(defaultValue = "10")
            @Min(value = 1L, message = "Page limit can't be less than 1")
            Integer pageSize,
            @RequestParam()
            @Min(value = 1L, message = "CategoryID can't be less than 1")
            Long categoryId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "id")
            @Pattern(regexp = "(?i)id|name", message = "sortBy must be one of: id, name")
            String sortBy,
            @RequestParam(required = false, defaultValue = "ASC")
            @Pattern(regexp = "(?i)asc|desc", message = "sortDirection must be one of: asc, desc")
            String sortDirection
    ) {
        Page<ProductResponseDto> response = productService.findAll(
                PageRequest.of(
                        pageNumber,
                        pageSize,
                        Sort.Direction.valueOf(sortDirection.toUpperCase()),
                        sortBy.toLowerCase()
                ),
                name,
                categoryId
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(
            @RequestBody @Validated ProductCreateRequestDto request,
            Principal principal
    ) {
        ProductResponseDto response = productService.create(request, principal.getName());

        return ResponseEntity
                .created(URI.create("/api/v1/product/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, Principal principal) {
        productService.delete(id, principal.getName());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(
            @RequestBody @Validated ProductUpdateRequestDto request,
            Principal principal
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.update(request, principal.getName()));
    }

    @PostMapping("/price")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addPriceToProduct(
            @RequestBody @Validated PriceCreateRequestDto request,
            Principal principal
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addPrice(request, principal.getName()));
    }

    @PostMapping(value = "/batch", consumes = "multipart/form-data")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> uploadProductsFile(
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) {
        ProductBatchUploadDto response =  productService.uploadProducts(file, principal.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(value = "/batch/price", consumes = "multipart/form-data")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> uploadPricesFile(
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) {
        PriceBatchUploadDto response =  productService.uploadPrices(file, principal.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
