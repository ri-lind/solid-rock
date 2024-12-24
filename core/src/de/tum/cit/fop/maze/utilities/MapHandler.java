package de.tum.cit.fop.maze.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.tum.cit.fop.maze.objects.EntryPoint;
import de.tum.cit.fop.maze.objects.Exit;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Wall;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * Contains methods to read and manipulate the map from the java properties file.
 * Instantiates the list of Map Objects
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
    public static Map<Integer, List<GameObject>> convertToMap(String mapContent){
        Map<Integer, List<GameObject>> mapMap = new HashMap<>();
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



            // objecttype, coordinates.
            GameObject gameObject = convertToGameObject(objectType, x, y);

            if (mapMap.containsKey(objectType)){
                mapMap.get(objectType).add(gameObject);
            } else{
                ArrayList<GameObject> initialElement = new ArrayList<>();
                initialElement.add(gameObject);
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
    public static String convertMapToString(Map<Integer, List<GameObject>> mapMap){
        // this was recommended by IntelliJ as fix for string not being mutable in lambda expression
        String[] mapString = {""};
        mapMap.forEach(
                (key, value) -> {
                    value.forEach(
                            gameObject -> {
                                String coordinates = "";
                                coordinates += gameObject.sprite.getX() + "," + gameObject.sprite.getY();
                                mapString[0] += key + "=" + coordinates + "\n";
                            }
                    );
                }
        );
        return mapString[0];
        }

        /*
        Value | Type
        -------------------
        0     | Wall
        1     | Entry point
        2     | Exit
        3     | Trap (static obstacle)
        4     | Enemy (dynamic obstacle)
        5     | Key
         */
        public static GameObject convertToGameObject(int objectType, float x, float y){
            GameObject gameObject;
            if (objectType == 0){
                gameObject = new Wall(x, y);
            } else if (objectType == 1) {
                gameObject = new EntryPoint(x, y);
            } else if(objectType == 2) {
                gameObject = new Exit(x,y);
            } else{
                // not defined gameobject. let's just create a wall tile in the middle of the map
                // worldwidth/2 dhe worldHeight/2
                gameObject = new Wall(400, 256);
            }

            return gameObject;
    }
};