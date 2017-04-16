package com.avion.spatialsystems.misc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

//Created by Bread10 at 09:08 on 16/04/2017
public class LogHelper {

    private static Logger logger;

    public static void setLogger(Logger log) {
        logger = log;
    }

    public static void log(Level level, String message) {
        logger.log(level, ">>> " + message);
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void warning(String message) {
        log(Level.WARN, message);
    }

    public static void error(String message) {
        log(Level.ERROR, message);
    }

}
