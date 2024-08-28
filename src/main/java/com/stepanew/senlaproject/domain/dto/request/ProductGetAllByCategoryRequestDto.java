package com.stepanew.senlaproject.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Dto для получения информации о продуктах, принадлежащих к одной категории, с пагинацией и фильтрацией")
@Getter
@Setter
public class ProductGetAllByCategoryRequestDto {

    @NotNull(message = "Page number must be not null")
    @Min(value = 0, message = "Page number can't be less than 0")
    @Schema(description = "Номер страницы", example = "0")
    private Integer pageNumber;
    @NotNull(message = "Page limit must be not null")
    @Min(value = 1, message = "Page limit can't be less than 1")
    @Schema(description = "Количество элементов на странице", example = "10")
    private Integer pageSize;

    @Min(value = 1L, message = "CategoryID can't be less than 1")
    @NotNull(message = "CategoryID must be not null")
    @Schema(description = "ID категории, по которой идет поиск", example = "1")
    private Long categoryId;

    @Schema(description = "Название продукта, по которому идет фильтрация", example = "Мясо")
    private String name = "";
    @Pattern(regexp = "(?i)id|name", message = "sortBy must be one of: id, name")
    @Schema(description = "Название поля, по которому идет фильтрация", example = "id")
    private String sortBy = "id";

    @Pattern(regexp = "(?i)asc|desc", message = "sortDirection must be one of: asc, desc")
    @Schema(description = "Порядок фильтрации (по убыванию или по возрастанию)", example = "ASC")
    private String sortDirection = "ASC";

}
