package de.tum.cit.fop.maze.objects;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.tum.cit.fop.maze.utilities.MapHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Does not actually represent a level currently, simply contains methods to draw the borders and the inner tiles.
 *
 * Ideally, in the future, this actually represents a level, with the various objects, contained in it.
 */
public class Level {

    public Map<Integer, List<GameObject>> gameObjects;
    private Player player;

    public Level(String fileName, Player player){
        String mapContent = MapHandler.readMapFromFile(fileName);
        Map<Integer, List<GameObject>> unscaledMap = MapHandler.convertToMap(mapContent);

        // it is scaled in the constructor
        this.gameObjects = MapHandler.scaleToWorld(unscaledMap);

        this.player = player;

    }

    /**
     * Draws the border tiles of the map. Is not bound to specific levels, so the method is static.
     * @param spriteBatch
     * @param rowBorderTile
     * @param columnBorderTile
     * @param camera
     */
    public static void drawBorderTiles(SpriteBatch spriteBatch, Sprite rowBorderTile,
                                       Sprite columnBorderTile, OrthographicCamera camera){
        // logic needed to draw the border tiles at the border lines of the camera
        float maxHeight = camera.viewportHeight;
        float maxWidth = camera.viewportWidth;

        // draw rows
        for (int i = 0; i <= maxWidth/rowBorderTile.getWidth(); i++) {
            rowBorderTile.setX(rowBorderTile.getWidth() * i);

            // draw tile at first row
            rowBorderTile.setY(0);
            rowBorderTile.draw(spriteBatch);

            // draw tile at last row
            rowBorderTile.setY(maxHeight-rowBorderTile.getHeight());
            rowBorderTile.draw(spriteBatch);
        }

        //draw columns
        for (int i = 0; i<= maxHeight/columnBorderTile.getWidth(); i++){
            columnBorderTile.setY(columnBorderTile.getHeight() * i);

            columnBorderTile.rotate(180);
            // draw tile at first column
            columnBorderTile.setX(0);
            columnBorderTile.draw(spriteBatch);

            columnBorderTile.rotate(180);
            // draw tile at last column
            columnBorderTile.setX(maxWidth - columnBorderTile.getWidth());
            columnBorderTile.draw(spriteBatch);


        }
    }

    /**
     * The method is not bound to levels, so it is static. Draws the inner tiles of the map.
     * @param spriteBatch
     * @param normalTile
     * @param camera
     */
    public static void drawNormalTiles(SpriteBatch spriteBatch, Sprite normalTile,
                                       OrthographicCamera camera){
        // the sprite starts at 0,0
        // we will start it at sprite.getWidth and getHeight
        // until camera.viewpointWidth - 2 * sprite.getWidth and same for height.

        // instead of starting at (0,0) we start at (presumably) (16,16)

        for (int i = 1; i <= camera.viewportWidth/normalTile.getWidth()-2; i++){
            for (int j =1; j<= camera.viewportHeight/normalTile.getHeight()-2; j++){
                normalTile.setX(normalTile.getWidth()* i);
                normalTile.setY(normalTile.getHeight()*j);

                normalTile.draw(spriteBatch);
            }
        }


    }

    /**
     * Draw the game objects of the level unto the screen
     * @param spriteBatch
     */
    public void drawGameObjects(SpriteBatch spriteBatch){
        //futuristic expression, cause I am a boss.
        gameObjects.forEach(
                (objectType, listOfObjects) -> {
                    listOfObjects.forEach(
                            object -> {
                                object.sprite.draw(spriteBatch);
                            }
                    );
                }
        );
    }

    public boolean collides(Player player){
        AtomicBoolean collides = new AtomicBoolean(false);

        this.gameObjects.forEach(
                (objectType, gameObjects) -> {
                    gameObjects.forEach(
                            gameObject -> {
                                // shortening to a one-liner does not work.
                                if (gameObject.collide(player))
                                    collides.set(true);
                            }
                    );
                }
        );
        return collides.get();
    }
}
