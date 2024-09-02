package ru.clevertec.check.controllers;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.exceptions.NotEnoughMoneyException;
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.exceptions.QuantityException;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.utils.CheckJsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckServletTest {
    @InjectMocks
    private CheckServlet checkServlet;
    @Mock
    private CheckService checkService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;


    @Test
    void testDoPostSuccess() throws IOException, NotEnoughMoneyException, SQLException,
            DiscountCardNotFoundException, QuantityException, ProductNotFoundException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("discountCard", "1111");
        requestBody.addProperty("balanceDebitCard", BigDecimal.valueOf(100.00));
        String expectedCheckString = "CheckString";

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(requestBody.toString()))
        );
        when(checkService.generateCheckRest(anyMap(), anyString(), any(BigDecimal.class)))
                .thenReturn(expectedCheckString);

        try (MockedStatic<CheckJsonParser> mockedStatic = mockStatic(CheckJsonParser.class)) {
            Map<Integer, Integer> idsAndQuantities = Map.of(1, 10);
            mockedStatic.when(() -> CheckJsonParser.getIdsAndQuantities(requestBody)).thenReturn(idsAndQuantities);


            checkServlet.doPost(request, response);


            verify(response).setContentType("text/plain");
            verify(writer, times(1)).write(expectedCheckString);
        }
    }

    @Test
    void testDoPostBadRequestStatus() throws IOException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("discountCard", "1111");
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(requestBody.toString()))
        );


        checkServlet.doPost(request, response);


        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPostNotFoundStatus() throws IOException, NotEnoughMoneyException, SQLException,
            DiscountCardNotFoundException, QuantityException, ProductNotFoundException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("discountCard", "1111");
        requestBody.addProperty("balanceDebitCard", BigDecimal.valueOf(100.00));

        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader(requestBody.toString()))
        );
        when(checkService.generateCheckRest(anyMap(), anyString(), any(BigDecimal.class)))
                .thenThrow(ProductNotFoundException.class);

        try (MockedStatic<CheckJsonParser> mockedStatic = mockStatic(CheckJsonParser.class)) {
            mockedStatic.when(() -> CheckJsonParser.getIdsAndQuantities(requestBody)).thenReturn(Collections.EMPTY_MAP);


            checkServlet.doPost(request, response);


            verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Test
    void testDoPostInternalServerErrorStatus() throws IOException {
        when(request.getReader()).thenThrow(IOException.class);

        checkServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}