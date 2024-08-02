package com.stepanew.senlaproject.controller.api;

import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;
import com.stepanew.senlaproject.exceptions.message.ErrorMessage;
import com.stepanew.senlaproject.exceptions.message.ValidationErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Price API", description = "API получения статистики поо стоимости товаров")
public interface PriceApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "График успешно создан",
                    content = @Content(
                            mediaType = "image/png",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет данных для создания графика"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - некоторый поля пустые",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизирован",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получение графика изменения цен на товар в определенном магазине")
    ResponseEntity<?> getPriceTrend(
            @Parameter(description = "ID продукта", example = "1")
            Long productId,
            @Parameter(description = "ID магазина", example = "1")
            Long storeId,
            @Parameter(description = "Дата начала в формате ISO", example = "2023-01-01T00:00:00")
            LocalDateTime startDate,
            @Parameter(description = "Дата окончания в формате ISO", example = "2025-12-31T23:59:59")
            LocalDateTime endDate
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Отчёт успешно создан",
                    content = @Content(
                            mediaType = "application/vnd.ms-excel",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет данных для создания отчёта"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - некоторый поля пустые",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизирован",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получение отчёта по изменению цен в виде таблицы Excel")
    ResponseEntity<?> getPriceReport(
            @Parameter(description = "ID продукта", example = "1") Long productId,
            @Parameter(description = "ID магазина", example = "1") Long storeId,
            @Parameter(description = "Дата начала в формате ISO", example = "2023-01-01T00:00:00")
            LocalDateTime startDate,
            @Parameter(description = "Дата окончания в формате ISO", example = "2025-12-31T23:59:59")
            LocalDateTime endDate
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Средняя цена успешно получена",
                    content = @Content(
                            mediaType = "image/png",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Нет данных для создания отчёта"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - некоторый поля пустые или некорректные",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизирован",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получение средней цены на товар за интервал времени")
    ResponseEntity<?> getAveragePriceBy(
            @Parameter(description = "ID продукта", example = "1") Long productId,
            @Parameter(description = "Период для усреднения (hours, days, months, years)", example = "hours")
            @Pattern(regexp = "(?i)hours|years|months|days", message = "averageBy must be one of: hours, days, months, years")
            String averageBy,
            @Parameter(description = "Дата начала в формате ISO", example = "2023-01-01T00:00:00")
            LocalDateTime startDate,
            @Parameter(description = "Дата окончания в формате ISO", example = "2025-12-31T23:59:59")
            LocalDateTime endDate
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Сравнение цен успешно выполнено",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PriceComparisonResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Не авторизирован",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Сравнение цен на товары в двух магазинах")
    ResponseEntity<?> comparePrices(
            @Parameter(description = "Список ID продуктов", example = "[1, 2, 3]")
            List<Long> productIds,
            @Parameter(description = "ID первого магазина", example = "1")
            @Min(value = 1L, message = "First store id must be more that 0")
            Long firstStoreId,
            @Parameter(description = "ID второго магазина", example = "2")
            @Min(value = 1L, message = "Second store id must be more that 0")
            Long secondStoreId
    );
}