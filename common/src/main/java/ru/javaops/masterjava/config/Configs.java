package ru.javaops.masterjava.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * gkislin
 * 01.11.2016
 */
public class Configs {

    private static final Config APP_CONFIG = getConfig("app.conf", "app");

    public static Config getConfig(String resource) {
        return ConfigFactory.parseResources(resource).resolve();
    }

    public static Config getConfig(String resource, String domain) {
        return getConfig(resource).getConfig(domain);
    }

    public static File getConfigFile(String fileName) {
        return new File(APP_CONFIG.getString("configDir") + fileName);
    }

    public static String getURL(String fileName)  {
       return APP_CONFIG.getString("configDir") + fileName;
    }
}
