package com.stepanew.senlaproject.controller;

import com.stepanew.senlaproject.domain.dto.response.ComparisonResponseDto;
import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;
import com.stepanew.senlaproject.domain.dto.response.StoreWithPriceResponseDto;
import com.stepanew.senlaproject.services.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PriceController.class)
class PriceControllerTest extends AbstractControllerTest {

    @MockBean
    private PriceService priceService;

    private byte[] dummyImage;

    private byte[] dummyReport;

    private static final String PATH = "/api/v1/price";

    private static final String PRODUCT_ID_PARAM = "productId";

    private static final String STORE_ID_PARAM = "storeId";

    private static final String START_DATE_PARAM = "startDate";

    private static final String END_DATE_PARAM = "endDate";

    private static final String AVERAGE_BY_PARAM = "averageBy";

    private static final String PRODUCT_IDS_PARAM = "productIds";

    private static final String FIRST_STORE_ID_PARAM = "firstStoreId";

    private static final String SECOND_STORE_ID_PARAM = "secondStoreId";

    private static final String PRODUCT_ID_VALUE = "1";

    private static final String STORE_ID_VALUE = "1";

    private static final String START_DATE_VALUE = "2024-01-01T00:00:00";

    private static final String END_DATE_VALUE = "2024-12-31T23:59:59";

    private static final String AVERAGE_BY_VALUE = "days";

    private static final String MEDIA_TYPE_XLSX = "application/vnd.ms-excel";

    private PriceComparisonResponseDto responseDto;

    @BeforeEach
    void setUp() {
        dummyImage = new byte[]{1, 2, 3};
        dummyReport = new byte[]{4, 5, 6};
        StoreWithPriceResponseDto minPrice = new StoreWithPriceResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_ADDRESS,
                DEFAULT_PRICE
        );
        StoreWithPriceResponseDto maxPrice = new StoreWithPriceResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_ADDRESS,
                DEFAULT_PRICE
        );
        ComparisonResponseDto comparisonResponseDto = new ComparisonResponseDto(
                DEFAULT_ID,
                DEFAULT_NAME,
                minPrice,
                maxPrice,
                DEFAULT_NAME
        );
        responseDto = new PriceComparisonResponseDto(new ArrayList<>(List.of(comparisonResponseDto)));
    }

    //The beginning of getPriceTrend method tests
    //Test 200 OK
    @Test
    @WithMockUser
    void getPriceTrendTest() throws Exception {
        when(priceService.getPriceTrend(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(dummyImage);

        mockMvc.perform(get(PATH + "/trend")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(dummyImage));
    }

    //Test 204 No Content
    @Test
    @WithMockUser
    void getPriceTrendNoContentTest() throws Exception {
        when(priceService.getPriceTrend(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new byte[0]);

        mockMvc.perform(get(PATH + "/trend")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isNoContent());
    }

    //Test 400 Bad Request
    @Test
    @WithMockUser
    void getPriceTrendBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/trend")
                        .param(PRODUCT_ID_PARAM, (String) null)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isBadRequest());
    }

    //Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void getPriceTrendUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/trend")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isUnauthorized());
    }
    //The end of getPriceTrend method tests

    //The beginning of getPriceReport method tests
    //Test 200 OK
    @Test
    @WithMockUser
    void getPriceReportTest() throws Exception {
        when(priceService.getPriceReport(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(dummyReport);

        mockMvc.perform(get(PATH + "/report")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"report.xlsx\""))
                .andExpect(content().contentType(MediaType.parseMediaType(MEDIA_TYPE_XLSX)))
                .andExpect(content().bytes(dummyReport));
    }

    //Test 204 No Content
    @Test
    @WithMockUser
    void getPriceReportNoContentTest() throws Exception {
        when(priceService.getPriceReport(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new byte[0]);

        mockMvc.perform(get(PATH + "/report")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isNoContent());
    }

    //Test 400 Bad Request
    @Test
    @WithMockUser
    void getPriceReportBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/report")
                        .param(PRODUCT_ID_PARAM, (String) null)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isBadRequest());
    }

    //Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void getPriceReportUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/report")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(STORE_ID_PARAM, STORE_ID_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isUnauthorized());
    }
    //The end of getPriceReport method tests

    //The beginning of getAveragePriceBy method tests
    //Test 200 OK
    @Test
    @WithMockUser
    void getAveragePriceByTest() throws Exception {
        when(priceService.getAveragePriceBy(
                anyLong(),
                eq(ChronoUnit.DAYS),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        )
                .thenReturn(dummyImage);

        mockMvc.perform(get(PATH + "/average")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(AVERAGE_BY_PARAM, AVERAGE_BY_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(dummyImage));
    }

    //Test 204 No Content
    @Test
    @WithMockUser
    void getAveragePriceByNoContentTest() throws Exception {
        when(priceService.getAveragePriceBy(
                anyLong(),
                eq(ChronoUnit.DAYS),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        )
                .thenReturn(new byte[0]);

        mockMvc.perform(get(PATH + "/average")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(AVERAGE_BY_PARAM, AVERAGE_BY_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isNoContent());
    }

    //Test 400 Bad Request
    @Test
    @WithMockUser
    void getAveragePriceByBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/average")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(AVERAGE_BY_PARAM, AVERAGE_BY_VALUE.repeat(2))
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isBadRequest());
    }

    //Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void getAveragePriceByUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/average")
                        .param(PRODUCT_ID_PARAM, PRODUCT_ID_VALUE)
                        .param(AVERAGE_BY_PARAM, AVERAGE_BY_VALUE)
                        .param(START_DATE_PARAM, START_DATE_VALUE)
                        .param(END_DATE_PARAM, END_DATE_VALUE))
                .andExpect(status().isUnauthorized());
    }
    //The end of getAveragePriceBy method tests

    //The beginning of comparePrices method tests
    //Test 200 OK
    @Test
    @WithMockUser
    void comparePricesTest() throws Exception {
        when(priceService.comparePrices(any(), anyLong(), anyLong())).thenReturn(responseDto);

        mockMvc.perform(get(PATH + "/comparison")
                        .param(PRODUCT_IDS_PARAM, Long.toString(DEFAULT_ID))
                        .param(FIRST_STORE_ID_PARAM, Long.toString(DEFAULT_ID))
                        .param(SECOND_STORE_ID_PARAM, Long.toString(DEFAULT_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    //Test 400 Bad Request
    @Test
    @WithMockUser
    void comparePricesBadRequestTest() throws Exception {
        mockMvc.perform(get(PATH + "/comparison")
                        .param(PRODUCT_IDS_PARAM, Long.toString(DEFAULT_ID))
                        .param(FIRST_STORE_ID_PARAM, Long.toString(0L))
                        .param(SECOND_STORE_ID_PARAM, Long.toString(0L)))
                .andExpect(status().isBadRequest());
    }

    //Test 401 Unauthorized
    @Test
    @WithAnonymousUser
    void comparePricesUnauthorizedTest() throws Exception {
        mockMvc.perform(get(PATH + "/comparison")
                        .param(PRODUCT_IDS_PARAM, Long.toString(DEFAULT_ID))
                        .param(FIRST_STORE_ID_PARAM, Long.toString(DEFAULT_ID))
                        .param(SECOND_STORE_ID_PARAM, Long.toString(DEFAULT_ID)))
                .andExpect(status().isUnauthorized());
    }
    //The end of comparePrices method tests

}
