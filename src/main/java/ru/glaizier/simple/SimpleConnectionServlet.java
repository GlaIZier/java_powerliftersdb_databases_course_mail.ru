package ru.glaizier.simple;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleConnectionServlet extends HttpServlet {

    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType(CONTENT_TYPE);
        response.getWriter().println("Hello from Simple Servlet!");
        response.getWriter().close();
    }
}