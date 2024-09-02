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
import ru.clevertec.check.exceptions.ProductNotFoundException;
import ru.clevertec.check.models.Product;
import ru.clevertec.check.models.builder.ProductBuilder;
import ru.clevertec.check.services.ProductService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private Product product = new ProductBuilder()
            .setId(1)
            .setDescription("prod1")
            .setPrice(new BigDecimal("0.45"))
            .setQuantity(5)
            .setWholesale(false)
            .build();

    @Nested
    class ProductDoGetTests {
        @Mock
        private PrintWriter writer;

        @BeforeEach
        void setUp() throws IOException {
            when(response.getWriter()).thenReturn(writer);
        }

        @Test
        void testDoGetSuccess() throws SQLException, ProductNotFoundException, JSONException {
            String expectedJson = new Gson().toJson(product);

            var captor = ArgumentCaptor.forClass(String.class);
            when(request.getParameter("id")).thenReturn(String.valueOf(product.getId()));
            when(productService.getProductById(product.getId())).thenReturn(product);

            productController.doGet(request, response);

            verify(response).setContentType("application/json");
            verify(writer, times(1)).print(captor.capture());
            verify(writer, times(1)).flush();

            JSONAssert.assertEquals(expectedJson, captor.getValue(), true);
        }

        @Test
        void testDoGetNotFoundStatus() throws SQLException, ProductNotFoundException {
            when(request.getParameter("id")).thenReturn(String.valueOf(product.getId()));
            when(productService.getProductById(anyInt())).thenThrow(ProductNotFoundException.class);

            productController.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        @Test
        void testDoGetBadRequestStatus() throws IOException {
            when(response.getWriter()).thenThrow(IOException.class);

            productController.doGet(request, response);

            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        @Test
        void testDoGetInternalServerErrorStatus() throws SQLException, ProductNotFoundException {
            when(request.getParameter("id")).thenReturn(String.valueOf(product.getId()));
            when(productService.getProductById(anyInt())).thenThrow(SQLException.class);

            productController.doGet(request, response);

            verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class ProductDoPostTests {
        @BeforeEach
        void setUp() throws IOException {
            when(request.getReader()).thenReturn(
                    new BufferedReader(new StringReader(new Gson().toJson(product)))
            );
        }

        @Test
        void testDoPostSuccess() throws SQLException {
            ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

            productController.doPost(request, response);

            verify(productService, times(1)).addProduct(captor.capture());
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);

            assertThat(captor.getValue().getId()).isEqualTo(product.getId());
        }

        @Test
        void testDoPostInternalServerErrorStatus() throws SQLException {
            doThrow(SQLException.class).when(productService).addProduct(any(Product.class));

            productController.doPost(request, response);

            verify(productService, times(1)).addProduct(any(Product.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class ProductDoPutTests {
        @BeforeEach
        void setUp() throws IOException {
            when(request.getReader()).thenReturn(
                    new BufferedReader(new StringReader(new Gson().toJson(product)))
            );
            when(request.getParameter("id")).thenReturn(String.valueOf(product.getId()));
        }

        @Test
        void testDoPutSuccess() throws SQLException {
            var captor = ArgumentCaptor.forClass(Product.class);

            productController.doPut(request, response);

            verify(productService, times(1)).updateProductById(anyInt(), captor.capture());
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);

            assertThat(captor.getValue().getId()).isEqualTo(product.getId());
        }

        @Test
        void testDoPutInternalServerErrorStatus() throws SQLException {
            doThrow(SQLException.class).when(productService).updateProductById(anyInt(), any(Product.class));

            productController.doPut(request, response);

            verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Nested
    class ProductDoDeleteTests {
        @BeforeEach
        void setUp() {
            when(request.getParameter("id")).thenReturn(String.valueOf(product.getId()));
        }

        @Test
        void testDoDeleteSuccess() throws SQLException {
            productController.doDelete(request, response);

            verify(productService, times(1)).deleteProductById(1);
            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        @Test
        void testDoDeleteInternalServerErrorStatus() throws SQLException {
            doThrow(SQLException.class).when(productService).deleteProductById(anyInt());

            productController.doDelete(request, response);

            verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}