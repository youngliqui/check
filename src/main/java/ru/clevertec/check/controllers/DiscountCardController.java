package ru.clevertec.check.controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.check.exceptions.DiscountCardNotFoundException;
import ru.clevertec.check.models.DiscountCard;
import ru.clevertec.check.services.DiscountCardService;
import ru.clevertec.check.services.factory.ServiceFactory;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DiscountCardServlet", urlPatterns = "/discountcards")
public class DiscountCardController extends HttpServlet {
    private DiscountCardService discountCardService;

    @Override
    public void init(ServletConfig config) {
        discountCardService = ServiceFactory.createDiscountCardService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (PrintWriter out = resp.getWriter()) {
            int id = Integer.parseInt(req.getParameter("id"));
            DiscountCard discountCard = discountCardService.getDiscountCardById(id);

            resp.setContentType("application/json");
            Gson gson = new Gson();
            out.print(gson.toJson(discountCard));
            out.flush();
        } catch (DiscountCardNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Gson gson = new Gson();
            DiscountCard discountCard = gson.fromJson(req.getReader(), DiscountCard.class);

            discountCardService.addDiscountCard(discountCard);

            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Gson gson = new Gson();
            DiscountCard discountCard = gson.fromJson(req.getReader(), DiscountCard.class);

            discountCardService.updateDiscountCardById(id, discountCard);

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            int id = Integer.parseInt(req.getParameter("id"));

            discountCardService.deleteDiscountCardById(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
