package ru.glaizier.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleConnectionServlet extends HttpServlet {

    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String SIMPLE_TEMPLATE_NAME = "simple.ftl";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType(CONTENT_TYPE);
//        try {
//            response.getWriter().println("Hello from Simple Servlet! " + new SimpleConnection().getConnection().getSchema());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        response.getWriter().close();
        request.getRequestDispatcher(SIMPLE_TEMPLATE_NAME).forward(request, response);
    }
}