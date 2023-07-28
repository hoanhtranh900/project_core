package com.osp.core.contants;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class ConfigProperties {
	private static final Properties properties = new Properties();

	public static String getConfigProperties(String key, String fileName){
		String value = key;
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName));
			if(properties.containsKey(key)){
				value = properties.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

    public static Properties loadFromXML(String pathFile) throws IOException {
        File file = new File(pathFile);
        Properties prop = new Properties();
        prop.loadFromXML(new FileInputStream(file));
        return prop;
    }

    public static Boolean changeXml(String pathFile, Map<String, String> map) throws IOException {
        File configFile = new File(pathFile);
        Properties prop = new Properties();
        prop.loadFromXML(new FileInputStream(configFile));
        OutputStream outputStream = new FileOutputStream(configFile);
        for ( String key : map.keySet() ) {
            prop.setProperty(key, map.get(key));
        }
        prop.storeToXML(outputStream, "change xml value success!");
        outputStream.close();
        return true;
    }
}

