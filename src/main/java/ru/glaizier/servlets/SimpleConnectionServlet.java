package ru.glaizier.servlets;

import ru.glaizier.simple.SimpleConnection;
import ru.glaizier.simple.domain.BiggestExercise;
import ru.glaizier.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SimpleConnectionServlet extends HttpServlet {

    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String SIMPLE_TEMPLATE_NAME = "simple.ftl";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType(CONTENT_TYPE);

        SimpleConnection simpleConnection = new SimpleConnection(Utils.getDbUrl(), Utils.getDbUserName(), Utils.getDbPassword());
        BiggestExercise biggestSquat = simpleConnection.getBiggestSquat();

        Map<String, Object> params = new HashMap<>();
        if (biggestSquat != null) {
            params.put("lastName", biggestSquat.getLastName());
            params.put("firstName", biggestSquat.getFirstName());
            params.put("sex", biggestSquat.getSex());
            params.put("birthday", biggestSquat.getBirthday());
            params.put("resultInKg", biggestSquat.getResultInKg());
        } else
            System.out.println("Warning: null has been returned from biggest squat!");

        request.setAttribute("params", params);
        request.getRequestDispatcher(SIMPLE_TEMPLATE_NAME).forward(request, response);
    }
}