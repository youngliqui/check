package ru.clevertec.check.controllers;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.models.builder.DiscountCardBuilder;
import ru.clevertec.check.services.DiscountCardService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountCardControllerTest {
    @InjectMocks
    private DiscountCardController discountCardController;
    @Mock
    private DiscountCardService discountCardService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter writer;


    @Nested
    class DiscountCardDoGetTests {
        @BeforeEach
        void setUp() throws IOException {
            when(response.getWriter()).thenReturn(writer);
        }

        @Test
        void testDoGetSuccess() throws SQLException, DiscountCardNotFoundException, JSONException {
            DiscountCard discountCard = new DiscountCardBuilder()
                    .setId(1)
                    .setNumber("1111")
                    .setDiscount(3)
                    .build();
            String expectedJson = new Gson().toJson(discountCard);

            when(request.getParameter("id")).thenReturn(String.valueOf(discountCard.getId()));
            when(discountCardService.getDiscountCardById(discountCard.getId())).thenReturn(discountCard);

            discountCardController.doGet(request, response);

            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(response, times(1)).setContentType("application/json");
            verify(writer, times(1)).print(captor.capture());
            verify(writer, times(1)).flush();

            JSONAssert.assertEquals(expectedJson, captor.getValue(), true);
        }

        @Test
        void testDoGetNotFoundStatus() throws SQLException, DiscountCardNotFoundException {
            when(request.getParameter("id")).thenReturn("1");
            when(discountCardService.getDiscountCardById(anyInt())).thenThrow(DiscountCardNotFoundException.class);

            discountCardController.doGet(request, response);

            verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        @Test
        void testDoGetBadRequestStatus() throws IOException {
            when(response.getWriter()).thenThrow(IOException.class);

            discountCardController.doGet(request, response);

            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        @Test
        void testDoGetInternalServerErrorStatus() {
            when(request.getParameter("id")).thenThrow(new RuntimeException("Internal Error"));

            discountCardController.doGet(request, response);

            verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class DiscountCardDoPostTests {
        private DiscountCard discountCard;

        @BeforeEach
        void setUp() throws IOException {
            discountCard = new DiscountCardBuilder()
                    .setId(2)
                    .setNumber("2222")
                    .setDiscount(2)
                    .build();
            when(request.getReader()).thenReturn(
                    new BufferedReader(new StringReader(new Gson().toJson(discountCard)))
            );
        }

        @Test
        void testDoPostSuccess() throws SQLException {
            ArgumentCaptor<DiscountCard> captor = ArgumentCaptor.forClass(DiscountCard.class);

            discountCardController.doPost(request, response);

            verify(discountCardService, times(1)).addDiscountCard(captor.capture());
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);

            assertThat(captor.getValue().getNumber()).isEqualTo(discountCard.getNumber());
        }

        @Test
        void testDoPostInternalServerErrorStatus() throws SQLException {
            doThrow(SQLException.class).when(discountCardService).addDiscountCard(any(DiscountCard.class));

            discountCardController.doPost(request, response);

            verify(discountCardService, times(1)).addDiscountCard(any(DiscountCard.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class DiscountCardDoPutTests {
        private DiscountCard discountCard;

        @BeforeEach
        void setUp() throws IOException {
            discountCard = new DiscountCardBuilder()
                    .setId(2)
                    .setNumber("2222")
                    .setDiscount(2)
                    .build();
            when(request.getReader()).thenReturn(
                    new BufferedReader(new StringReader(new Gson().toJson(discountCard)))
            );
            when(request.getParameter("id")).thenReturn(String.valueOf(discountCard.getId()));
        }

        @Test
        void testDoPutSuccess() throws SQLException {
            ArgumentCaptor<DiscountCard> captor = ArgumentCaptor.forClass(DiscountCard.class);

            discountCardController.doPut(request, response);

            verify(discountCardService, times(1)).updateDiscountCardById(anyInt(), captor.capture());
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);

            assertThat(captor.getValue().getId()).isEqualTo(discountCard.getId());
        }

        @Test
        void testDoPutInternalServerErrorStatus() throws SQLException {
            doThrow(SQLException.class).when(discountCardService).updateDiscountCardById(anyInt(), any(DiscountCard.class));

            discountCardController.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class DiscountCardDoDeleteTests {
        @BeforeEach
        void setUp() {
            when(request.getParameter("id")).thenReturn("1");
        }

        @Test
        void testDoDeleteSuccess() throws SQLException {
            discountCardController.doDelete(request, response);

            verify(discountCardService, times(1)).deleteDiscountCardById(1);
            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        @Test
        void testDoDeleteInternalServerErrorStatus() throws SQLException {
            doThrow(SQLException.class).when(discountCardService).deleteDiscountCardById(anyInt());

            discountCardController.doDelete(request, response);

            verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}