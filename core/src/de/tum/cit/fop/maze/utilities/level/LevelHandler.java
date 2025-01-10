package de.tum.cit.fop.maze.utilities.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.utilities.ScalingFactor;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Contains methods to read and manipulate the map from the java properties file.
 * Instantiates the list of Map Objects
 */
public class LevelHandler {

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


    public static Map<Integer, List<GameObject>> createGameObjects(String mapContent, FitViewport fitViewport){
        Map<Integer, List<GameObject>> map = convertToMap(mapContent);
        ScalingFactor scalingFactor = calculateScalingFactor(map, fitViewport);

        Map<Integer, List<GameObject>> newMap = new HashMap<>();
        map.forEach(
                (key, value) ->{
                    newMap.put(key, new ArrayList<>());
                    value.forEach(
                            gameObject ->  {
                                gameObject = GameObject.convertToGameObject(key, gameObject.sprite.getX(), gameObject.sprite.getY(), scalingFactor);
                                newMap.get(key).add(gameObject);
                            }
                    );
                }
        );

        return newMap;
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

            // need to apply scaling before drawing, so enemy hitbox is also scaled.
            GameObject gameObject = GameObject.convertToGameObject(objectType, x, y, new ScalingFactor());

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
    public static ScalingFactor calculateScalingFactor(Map<Integer, List<GameObject>> map,
                                                              FitViewport fitViewport){
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
        float width_scaling_factor = fitViewport.getWorldWidth()/maxX.get();
        float height_scaling_factor = fitViewport.getWorldHeight()/maxY.get();

        return new ScalingFactor(width_scaling_factor, height_scaling_factor);

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
                                mapString[0] += coordinates + "=" + key + "\n";
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
        6     | Border Tile
        7     | Super Power (Random Kill)
        8     | Heart (Collectable Life)
        9     | Book of Spawn (third obstacle)
         */

};