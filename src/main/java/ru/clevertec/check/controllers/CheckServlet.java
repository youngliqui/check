package ru.clevertec.check.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.exceptions.*;
import ru.clevertec.check.services.CheckService;
import ru.clevertec.check.services.factory.ServiceFactory;

import java.math.BigDecimal;
import java.util.Optional;

import static ru.clevertec.check.utils.CheckJsonParser.getIdsAndQuantities;

@WebServlet(name = "CheckServlet", urlPatterns = "/check")
public class CheckServlet extends HttpServlet {
    private CheckService checkService;

    @Override
    public void init(ServletConfig config) {
        checkService = ServiceFactory.createCheckService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Gson gson = new Gson();
            JsonObject requestBody = gson.fromJson(req.getReader(), JsonObject.class);

            String discountCardNumber = requestBody.get("discountCard").getAsString();

            BigDecimal balanceDebitCard = Optional.ofNullable(requestBody.get("balanceDebitCard"))
                    .map(JsonElement::getAsBigDecimal)
                    .orElseThrow(() -> new InvalidInputException("balance debit card must not be null"));

            String checkString = checkService.generateCheckRest(
                    getIdsAndQuantities(requestBody), discountCardNumber, balanceDebitCard
            );

            resp.setContentType("text/plain");
            resp.getWriter().write(checkString);

        } catch (InvalidInputException | QuantityException | NotEnoughMoneyException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ProductNotFoundException | DiscountCardNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
