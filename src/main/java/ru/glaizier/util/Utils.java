package ru.glaizier.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Utils {

    private static final Utils instance = new Utils();

    private Context context;

    private Utils() {
        try {
            context = InitialContext.doLookup("java:comp/env");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private static Context getContext() {
        return instance.context;
    }

    private static Object lookup(String envParam) {
        try {
            return getContext().lookup(envParam);
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DataSource getDataSource() {
        return (DataSource) lookup("jdbc/postgres");
    }

    public static String getDbUrl() {
        return (String) lookup("db.url");
    }

    public static String getDbServerName() {
        return (String) lookup("db.servername");
    }

    public static String getDbName() {
        return (String) lookup("db.name");
    }

    public static String getDbUserName() {
        return (String) lookup("db.username");
    }

    public static String getDbPassword() {
        return (String) lookup("db.password");
    }

    public static String getSimplePath() {
        return (String) lookup("path.simple");
    }

    public static String getPpoolPath() {
        return (String) lookup("path.ppool");
    }

    public static String getTpoolPath() {
        return (String) lookup("path.tpool");
    }

    public static String getHibernate() {
        return (String) lookup("path.hibernate");
    }

}
