package de.tum.cit.fop.maze.objects;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.utilities.LoaderHelper;
import de.tum.cit.fop.maze.utilities.MapHandler;

import java.util.ArrayList;
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

    public List<Sprite> tiles;

    Sprite normalTileType; // check size
    Sprite rowBorderTileType; // check size
    Sprite columnBorderTileType; // check size

    public Level(String fileName, Player player, OrthographicCamera camera){

        // loads key, enemies, traps, exits, entrances into  the world.
        String mapContent = MapHandler.readMapFromFile(fileName);
        Map<Integer, List<GameObject>> unscaledMap = MapHandler.convertToMap(mapContent);
        this.gameObjects = MapHandler.scaleToWorld(unscaledMap, camera);

        this.player = player;


        this.normalTileType = LoaderHelper.loadNormalBackgroundTile();
        this.rowBorderTileType = LoaderHelper.loadBackgroundBorderTile();
        this.columnBorderTileType = new Sprite(rowBorderTileType);
        this.columnBorderTileType.rotate90(true);
        this.tiles = new ArrayList<>();
        loadBorderTiles(camera);
        loadInnerTiles(camera);


    }

    /**
     * Method which loads the border sprites of the map into the list of tiles.
     * The list contains sprites. The list is later used to draw the level.
     * @param camera
     */
    public void loadBorderTiles(OrthographicCamera camera){
        // used for drawing sprites only until bounds.
        float maxHeight = camera.viewportHeight;
        float maxWidth = camera.viewportWidth;

        // draw rows
        for (int i = 0; i <= maxWidth/rowBorderTileType.getWidth(); i++) {
            rowBorderTileType.setX(rowBorderTileType.getWidth() * i);

            // load sprite of first row
            Sprite lowerBorderSprite = new Sprite(rowBorderTileType);
            lowerBorderSprite.setY(0);

            // load sprite of last row
            Sprite upperBorderSprite = new Sprite(rowBorderTileType);
            upperBorderSprite.setY(maxHeight - rowBorderTileType.getHeight());

            // add the sprites to the tiles list.

            this.tiles.add(lowerBorderSprite);
            this.tiles.add(upperBorderSprite);

        }

        //load columns
        for (int i = 0; i<= maxHeight/columnBorderTileType.getWidth(); i++){
            columnBorderTileType.setY(columnBorderTileType.getHeight() * i);

            // load sprite of first column
            Sprite leftBorderSprite = new Sprite(columnBorderTileType);
            leftBorderSprite.rotate(180);
            leftBorderSprite.setX(0);

            // load sprite of last column
            Sprite rightBorderSprite = new Sprite(columnBorderTileType);
            rightBorderSprite.setX(maxWidth - columnBorderTileType.getWidth());

            this.tiles.add(leftBorderSprite);
            this.tiles.add(rightBorderSprite);
        }
    }

    /**
     * Storing the inner sprites in the tiles list, so that they can be drawn and used for spawning later.
     * @param camera
     */
    public void loadInnerTiles(OrthographicCamera camera){
        // the sprite starts at 0,0
        // we will start it at sprite.getWidth and getHeight
        // until camera.viewpointWidth - 2 * sprite.getWidth and same for height.

        // instead of starting at (0,0) we start at (presumably) (16,16)

        for (int i = 1; i <= camera.viewportWidth/this.normalTileType.getWidth()-2; i++){
            for (int j =1; j<= camera.viewportHeight/this.normalTileType.getHeight()-2; j++){
                // create sprite with coordinate at indices, and add it to list.
                Sprite temporarySprite = new Sprite(this.normalTileType);
                temporarySprite.setX(temporarySprite.getWidth()* i);
                temporarySprite.setY(temporarySprite.getHeight()*j);

                this.tiles.add(temporarySprite);

            }
        }


    }

    /**
     * Draw the game objects of the level unto the screen
     * @param spriteBatch
     */
    public void drawGameObjects(SpriteBatch spriteBatch){
        gameObjects.forEach(
                (objectType, listOfObjects) -> {
                    listOfObjects.forEach(
                            object -> {
                                object.draw(spriteBatch);
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
                                if (gameObject.collide(player)){ // this line sets the trap to triggered
                                    if(gameObject.getClass() == Trap.class){
                                        Trap trap = (Trap) gameObject;
                                        System.out.println(trap.triggered);
                                    }
                                    collides.set(true);
                                }
                            }
                    );
                }
        );
        return collides.get();
    }
}
