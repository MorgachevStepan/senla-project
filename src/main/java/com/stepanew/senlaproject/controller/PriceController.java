package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.controller.api.PriceApi;
import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;
import com.stepanew.senlaproject.services.PriceService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/price")
@RequiredArgsConstructor
public class PriceController implements PriceApi {

    private final PriceService priceService;

    @GetMapping("/trend")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getPriceTrend(
            @RequestParam Long productId,
            @RequestParam Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        byte[] response = priceService.getPriceTrend(productId, storeId, startDate, endDate);

        if (response.length == 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(response);
    }

    @GetMapping("/report")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getPriceReport(
            @RequestParam Long productId,
            @RequestParam Long storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        byte[] response = priceService.getPriceReport(productId, storeId, startDate, endDate);

        if (response.length == 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity.ok()
                .contentLength(response.length)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.xlsx\"")
                .body(response);
    }

    @GetMapping("/average")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> getAveragePriceBy(
            @RequestParam Long productId,
            @RequestParam
            @Pattern(regexp = "(?i)hours|years|months|days", message = "averageBy must be one of: hours, days, months, years")
            String averageBy,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        byte[] response = priceService.getAveragePriceBy(
                productId,
                ChronoUnit.valueOf(averageBy.toUpperCase()),
                startDate,
                endDate
        );

        if (response.length == 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(response);
    }

    @GetMapping("/comparison")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<?> comparePrices(
            @RequestParam
            List<Long> productIds,
            @RequestParam
            @Min(value = 1L, message = "First store id must be more that 0")
            Long firstStoreId,
            @RequestParam
            @Min(value = 1L, message = "Second store id must be more that 0")
            Long secondStoreId
    ) {
        PriceComparisonResponseDto response = priceService.comparePrices(
                productIds,
                firstStoreId,
                secondStoreId
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
