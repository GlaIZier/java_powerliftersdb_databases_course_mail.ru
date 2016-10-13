package ru.glaizier.servlets;

import ru.glaizier.domain.FirstPowerlifterAfterDate;
import ru.glaizier.hibernate.HibernateDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirstPowerlifterAfterDateServlet extends HttpServlet {

    public static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    public static final String FIRST_POWERLIFTER_AFTER_DATE_FTL = "first_powerlifter_after_date.ftl";

    private HibernateDao hibernateDao;

    private Date date;


    @Override
    public void init() throws ServletException {
        hibernateDao = new HibernateDao();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
    }

    @Override
    public void destroy() {
        hibernateDao.destroy();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType(CONTENT_TYPE);

        // call all methods just to make sure they all work
        hibernateDao.testHibernateCityMapping();
        hibernateDao.testHibernatePowerliftingMapping();

        hibernateDao.testJpa();
        hibernateDao.testQueryApi();
        hibernateDao.testQueryApiObject();
        hibernateDao.testJpaCriteriaJoin();
        hibernateDao.testJpaCriteriaSubquery(date);

        hibernateDao.getFirstPowerlifterAfterDateHql(date);
        hibernateDao.getFirstPowerlifterAfterDateNative(date);
        FirstPowerlifterAfterDate firstPowerlifterAfterDate = hibernateDao.getFirstPowerlifterAfterDateJpaCriteria(date);

        Map<String, Object> params = new HashMap<>();
        params.put("date", date.getTime());
        params.put("id", firstPowerlifterAfterDate.getId());
        params.put("lastName", firstPowerlifterAfterDate.getLastName());
        params.put("firstName", firstPowerlifterAfterDate.getFirstName());
        params.put("sex", firstPowerlifterAfterDate.getSex().getSexName());
        params.put("birthdate", firstPowerlifterAfterDate.getBirthdate());
        params.put("birthplace", firstPowerlifterAfterDate.getBirthplace());

        request.setAttribute("params", params);
        request.getRequestDispatcher(FIRST_POWERLIFTER_AFTER_DATE_FTL).forward(request, response);
    }


}
