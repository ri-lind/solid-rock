package de.tum.cit.fop.maze.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Contains methods to read and manipulate the map from the java properties file.
 */
public class MapHandler {

    /**
     * Reads the map from the given file path and returns the content.
     * @param mapFilePath
     * @return the content of the map file in string format
     */
    public static String readMapFromFile (String mapFilePath) throws GdxRuntimeException {
        FileHandle mapFile = Gdx.files.internal(mapFilePath);
        return mapFile.readString(); // returns the content of the file
    }

    /**
     * Takes in map string and returns a map.
     * @param mapContent
     * @return
     */
    public static Map<Integer, List<Vector2>> convertToMap(String mapContent){
        Map<Integer, List<Vector2>> mapMap = new HashMap<>();
         Properties properties = new Properties();
         try{
             properties.load(new StringReader(mapContent));
         } catch (IOException e) {
             System.out.println("Error: " + e);
         }

        properties.forEach((key, value) -> {
            // we convert the object type to an integer
            String temporaryString = (String) value;
            int objectType = Integer.parseInt(temporaryString); // this line is cursed ahahhaaa

            String coordinatesString = (String) key;
            String[] coordinates = coordinatesString.split(",");

            // some more cursed conversion lines
            float x = Float.parseFloat(coordinates[0]);
            float y = Float.parseFloat(coordinates[1]);

            Vector2 coordinatesVector = new Vector2(x, y);

            if (mapMap.containsKey(objectType)){
                mapMap.get(objectType).add(coordinatesVector);
            } else{
                ArrayList<Vector2> initialElement = new ArrayList<Vector2>();
                initialElement.add(coordinatesVector);
                mapMap.put(objectType, initialElement);
            }

        });
        return mapMap;
    }

    /**
     * Converts a given level in a map format into string. Especially useful for logging.
     * @param mapMap
     * @return
     */
    public static String convertMapToString(Map<Integer, List<Vector2>> mapMap){
        // this was recommended by IntelliJ as fix for string not being mutable in lambda expression
        String[] mapString = {""};
        mapMap.forEach(
                (key, value) -> {
                    value.forEach(
                            vector2 -> {
                                String coordinates = "";
                                coordinates += vector2.x + "," + vector2.y;
                                mapString[0] += key + "=" + coordinates + "\n";
                            }
                    );
                }
        );
        return mapString[0];
        }
}