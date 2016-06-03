package ru.glaizier.servlets;

import ru.glaizier.dao.BiggestSquatDao;
import ru.glaizier.domain.BiggestExercise;
import ru.glaizier.pool.postgres.PostgresConnectionPool;
import ru.glaizier.pool.tomcat.TomcatConnectionPool;
import ru.glaizier.simple.SimpleConnection;
import ru.glaizier.util.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BiggestSquatServlet extends HttpServlet {

    private static String PPOOL_PATH = Utils.getPpoolPath();
    private static String TPOOL_PATH = Utils.getTpoolPath();

    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String SIMPLE_TEMPLATE_NAME = "biggest_squat.ftl";

    private BiggestSquatDao simpleConnection;
    private BiggestSquatDao postgresConnectionPool;
    private BiggestSquatDao tomcatConnectionPool;

    @Override
    public void init() throws ServletException {
        simpleConnection = new SimpleConnection(Utils.getDbUrl(), Utils.getDbUserName(), Utils.getDbPassword());
        postgresConnectionPool = new PostgresConnectionPool(Utils.getDbServerName(), Utils.getDbName(),
                Utils.getDbUserName(), Utils.getDbPassword());
        tomcatConnectionPool = new TomcatConnectionPool();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType(CONTENT_TYPE);

        BiggestExercise biggestSquat;
        if (request.getServletPath().equals(PPOOL_PATH))
            biggestSquat = postgresConnectionPool.getBiggestSquat();
        else if (request.getServletPath().equals(TPOOL_PATH))
            biggestSquat = tomcatConnectionPool.getBiggestSquat();
        else
            biggestSquat = simpleConnection.getBiggestSquat();

        Map<String, Object> params = new HashMap<>();
        if (biggestSquat != null) {
            params.put("lastName", biggestSquat.getLastName());
            params.put("firstName", biggestSquat.getFirstName());
            params.put("sex", biggestSquat.getSex());
            params.put("birthday", biggestSquat.getBirthday());
            params.put("resultInKg", biggestSquat.getResultInKg());
        } else
            System.out.println("Warning: null has been returned from " + request.getServletPath());

        request.setAttribute("params", params);
        request.getRequestDispatcher(SIMPLE_TEMPLATE_NAME).forward(request, response);
    }
}
