package de.tum.cit.fop.maze.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Contains methods to read the map from the java properties file.
 */
public class MapReader {

    public static void placeholder() throws IOException {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("/myProp.properties");
        prop.load(stream);

    }

}
