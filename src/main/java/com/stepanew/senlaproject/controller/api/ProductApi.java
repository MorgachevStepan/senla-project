package com.stepanew.senlaproject.controller.api;

import com.stepanew.senlaproject.domain.dto.request.PriceCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.ProductUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.PriceBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.PriceResponseDto;
import com.stepanew.senlaproject.domain.dto.response.ProductBatchUploadDto;
import com.stepanew.senlaproject.domain.dto.response.ProductResponseDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Tag(name = "Product API", description = "API работы с продуктами")
public interface ProductApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Данные о продукте успешно получены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - ID не должен быть пустым",
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт с таким id не найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получение продукта по его ID")
    ResponseEntity<?> getById(
            @Parameter(description = "ID продукта", example = "1")
            Long id
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Продукты успешно получены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Данная страница с продуктами пуста"
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
    @Operation(summary = "Получение всех продуктов из одной категории с пагинацией и фильтрацией")
    ResponseEntity<?> getAllByCategory(
            @Parameter(description = "Номер страницы", example = "1")
            @Min(value = 0L, message = "Page number can't be less than 0")
            Integer pageNumber,
            @Parameter(description = "Размер страницы", example = "10")
            @Min(value = 1L, message = "Page limit can't be less than 1")
            Integer pageSize,
            @Parameter(description = "ID категории", example = "1")
            @Min(value = 1L, message = "CategoryID can't be less than 1")
            Long categoryId,
            @Parameter(description = "Наименование продукта", example = "")
            String name,
            @Parameter(description = "Сортировка по параметру (id, name)", example = "id")
            @Pattern(regexp = "(?i)id|name", message = "sortBy must be one of: id, name")
            String sortBy,
            @Parameter(description = "Направление сортировки (asc, desc)", example = "asc")
            @Pattern(regexp = "(?i)asc|desc", message = "sortDirection must be one of: asc, desc")
            String sortDirection
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Продукт был успешно создан",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Форма для создания продукта")
    ResponseEntity<?> createProduct(
            @RequestBody @Validated ProductCreateRequestDto request,
            Principal principal
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Продукт был успешно удален"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - ID не должен быть пустым",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт по переданному id не был найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "418",
                    description = "Не трогай меня, я чайник!",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Удаление продукта по его ID")
    ResponseEntity<?> deleteProduct(
            @Parameter(description = "ID продукта", example = "1")
            Long id,
            Principal principal
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление продукта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductResponseDto.class)
                            )
                    }
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Имя продукта занято",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Форма для обновления данных продукта")
    ResponseEntity<?> updateProduct(
            @RequestBody @Validated ProductUpdateRequestDto request,
            Principal principal
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление цены для продукта",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PriceResponseDto.class)
                            )
                    }
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт или торговая точка не найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Форма для добавления цены на продукт")
    ResponseEntity<?> addPriceToProduct(
            @RequestBody @Validated PriceCreateRequestDto request,
            Principal principal
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное добавление продуктов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductBatchUploadDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат файла или неверный формат данных в файле",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт или торговая точка не найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Пакетное добавление данных о продуктах")
    ResponseEntity<?> uploadProductsFile(
            @Parameter(
                    description = "Файл с данными о продуктах в формате XLSX",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data")
            )
            MultipartFile file,
            Principal principal
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное добавление цен на продукты",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PriceBatchUploadDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный формат файла или неверный формат данных в файле",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
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
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ к методу не доступен",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Продукт или торговая точка не найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка сервера",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Пакетное добавление данных о ценах на продукты")
    ResponseEntity<?> uploadPricesFile(
            @Parameter(
                    description = "Файл с данными о ценах в формате XLSX",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data")
            )
            @RequestParam("file") MultipartFile file,
            Principal principal
    );

}
