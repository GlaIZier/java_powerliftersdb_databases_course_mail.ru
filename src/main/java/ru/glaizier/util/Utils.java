package ru.glaizier.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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

    private static String lookup(String envParam) {
        try {
            return (String) getContext().lookup(envParam);
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDbUrl() {
        return lookup("db.url");
    }

    public static String getDbServerName() {
        return lookup("db.servername");
    }

    public static String getDbName() {
        return lookup("db.name");
    }

    public static String getDbUserName() {
        return lookup("db.username");
    }

    public static String getDbPassword() {
        return lookup("db.password");
    }
}
