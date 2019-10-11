package ru.javaops.masterjava.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

/**
 * gkislin
 * 01.11.2016
 */
public class Configs {

    public static Config getConfig(String resource) {
        return ConfigFactory.parseResources(resource).resolve();
    }

    public static Config getConfig(String resource, String domain) {
        return getConfig(resource).getConfig(domain);
    }

    public static File getConfigFile(String fileName) {
        Config conf = ConfigFactory.parseResources("../resources/wsdls.conf");
        return new File(conf.getString("root") + fileName);
    }
}
