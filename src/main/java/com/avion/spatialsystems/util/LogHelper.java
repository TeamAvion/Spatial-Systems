package com.avion.spatialsystems.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

//Created by Bread10 at 09:08 on 16/04/2017
public class LogHelper {

    private static Logger logger;

    public static void setLogger(Logger log) {
        logger = log;
    }

    public static void log(Level level, Object message) {
        logger.log(level, ">>> " + message.toString());
    }

    public static void info(Object message) {
        log(Level.INFO, message);
    }

    public static void warning(Object message) {
        log(Level.WARN, message);
    }

    public static void error(Object message) {
        log(Level.ERROR, message);
    }

    public static void array(Level level, Object[] array) {
        String str = "[" + array[0].toString();
        for (int i = 1; i < array.length; i++) {
            str += ", " + array[i].toString();
        }
        str += "]";
        log(level, str);
    }

}
