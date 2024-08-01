package com.stepanew.senlaproject.controller.api;

import com.stepanew.senlaproject.domain.dto.request.StoreCreateRequestDto;
import com.stepanew.senlaproject.domain.dto.request.StoreUpdateRequestDto;
import com.stepanew.senlaproject.domain.dto.response.StoreResponseDto;
import com.stepanew.senlaproject.exceptions.message.ErrorMessage;
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

@Tag(name = "Store API", description = "API для работы с торговыми точками")
public interface StoreApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Торговая точка успешно получена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - ID не должен быть пустым",
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
                    responseCode = "404",
                    description = "Торговая точка с таким id не найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получение торговой точки по её ID")
    ResponseEntity<?> getById(
            @Parameter(description = "ID торговой точки", example = "1")
            Long id
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Тогровые точки успешно получены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - некоторый поля пустые или некорректные",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Данная страница с торговыми точками пуста"
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
    @Operation(summary = "Получение всех торговых точек с пагинацией и фильтрацией")
    ResponseEntity<?> getAll(
            @Parameter(description = "Номер страницы", example = "1")
            @Min(value = 0L, message = "Page number can't be less than 0")
            Integer pageNumber,
            @Parameter(description = "Размер страницы", example = "10")
            @Min(value = 1L, message = "Page limit can't be less than 1")
            Integer pageSize,
            @Parameter(description = "Название торговой точки", example = "")
            String name,
            @Parameter(description = "Адрес торговой точки", example = "")
            String address,
            @Parameter(description = "Сортировка по параметру (id, name, address)", example = "id")
            @Pattern(regexp = "(?i)id|name|address", message = "sortBy must be one of: id, name, address")
            String sortBy,
            @Parameter(description = "Направление сортировки (asc, desc)", example = "asc")
            @Pattern(regexp = "(?i)asc|desc", message = "sortDirection must be one of: asc, desc")
            String sortDirection
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Торговая точка была успешно создана",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - некоторый поля пустые или некорректные",
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
            )
    })
    @Operation(summary = "Форма для создания торговой точки")
    ResponseEntity<?> createStore(
            @RequestBody @Validated StoreCreateRequestDto request
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Торговая точка была успешно удалена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - ID не должен быть пустым",
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
                    description = "Торговая точка по переданному id не была найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Удаление торговой точки по ее ID")
    ResponseEntity<?> deleteStore(
            @Parameter(description = "ID торговой точки", example = "1")
            Long id
    );

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление торговой точки",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StoreResponseDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации входных данных - некоторый поля пустые или некорректные",
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
                    description = "Торговая точка не найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    }
            )
    })
    @Operation(summary = "Форма для обновления данных торговой точки")
    ResponseEntity<?> updateStore(
            @RequestBody @Validated StoreUpdateRequestDto request
    );

}
