package de.tum.cit.fop.maze.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.tum.cit.fop.maze.objects.EntryPoint;
import de.tum.cit.fop.maze.objects.Exit;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Wall;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Contains methods to read and manipulate the map from the java properties file.
 * Instantiates the list of Map Objects
 */
public class MapHandler {

    static final int WORLD_WIDTH = 800;
    static final int WORLD_HEIGHT = 512;
    static final int OBJECT_SCALE = 2;
    /**
     *
     * @param fileName
     * @return
     * @throws GdxRuntimeException
     */
    public static String readMapFromFile (String fileName) throws GdxRuntimeException {
        FileHandle mapFile = Gdx.files.internal(fileName);
        return mapFile.readString(); // returns the content of the file
    }

    /**
     * Convert the coordinates and object types into a java map object.
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
            GameObject gameObject = GameObject.convertToGameObject(objectType, x, y, OBJECT_SCALE);

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
     * Scales the coordinates to the world width and height.
     * @param map
     * @return
     */
    public static Map<Integer, List<GameObject>> scaleToWorld(Map<Integer, List<GameObject>> map){
        //get the maximum x and y from the coordinates
        AtomicReference<Float> maxX = new AtomicReference<>((float) 0);
        AtomicReference<Float> maxY = new AtomicReference<>((float) 0);

        map.forEach(
                (objectType, listofObjects) -> {
                    listofObjects.forEach(
                            gameObject -> {
                                float x = gameObject.sprite.getX();
                                float y = gameObject.sprite.getY();

                                if(x >= maxX.get())
                                    maxX.set(x);
                                if(y >= maxY.get())
                                    maxY.set(y);
                            }
                    );
                }
        );

        // calculate the scaling factor
        float width_scaling_factor = WORLD_WIDTH/maxX.get();
        float height_scaling_factor = WORLD_HEIGHT/maxY.get();

        System.out.println(maxX.get() + " " + maxY.get());
        //System.out.println(width_scaling_factor + " " + height_scaling_factor);

        // apply the scaling to the coordinates
        map.forEach(
                (objectType, gameObjects) -> {
                    gameObjects.forEach(
                            (gameObject) -> {
                                gameObject.sprite.setX(gameObject.sprite.getX() * width_scaling_factor);
                                gameObject.sprite.setY(gameObject.sprite.getY() * height_scaling_factor);
                            }
                    );
                }
        );
        return map;
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

};