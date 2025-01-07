package de.tum.cit.fop.maze.objects;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.GameScreen;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.collectables.Life;
import de.tum.cit.fop.maze.objects.collectables.RandomKill;
import de.tum.cit.fop.maze.objects.enemy.Enemy;
import de.tum.cit.fop.maze.objects.hud.ExitArrow;
import de.tum.cit.fop.maze.objects.obstacles.Trap;
import de.tum.cit.fop.maze.utilities.LoaderHelper;
import de.tum.cit.fop.maze.utilities.level.LevelHandler;
import de.tum.cit.fop.maze.utilities.SoundHandler;
import de.tum.cit.fop.maze.utilities.level.MapCreator;

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

    public int numberOfKeys;
    public int playerScore;
    public Map<Integer, List<GameObject>> gameObjects;
    private Player player;

    public List<Sprite> tiles;

    Sprite normalTileType; // check size
    Sprite rowBorderTileType; // check size
    Sprite columnBorderTileType; // check size
    public ExitArrow exitArrow;

    public List<Key> keys;
    public GameScreen gameScreen;

    public SoundHandler soundHandler;
    public FitViewport fitViewport;
    public MapCreator mapCreator;

    public Level(String fileName, FitViewport fitViewport, GameScreen gameScreen){

        this.gameScreen = gameScreen;
        this.fitViewport = fitViewport;
        this.mapCreator = new MapCreator(this); // order is important
        // loads key, enemies, traps, exits, entrances into  the world.
        String mapContent = LevelHandler.readMapFromFile(fileName);
        this.gameObjects = LevelHandler.createGameObjects(mapContent, fitViewport);
        setNumberOfKeys(); // sets number of keys
        this.normalTileType = LoaderHelper.loadNormalBackgroundTile();
        this.rowBorderTileType = LoaderHelper.loadBackgroundBorderTile();
        this.columnBorderTileType = new Sprite(rowBorderTileType);
        this.columnBorderTileType.rotate90(true);
        this.tiles = new ArrayList<>();
        this.gameObjects.put(6, loadBorderTiles((OrthographicCamera) fitViewport.getCamera()));
        loadInnerTiles((OrthographicCamera) fitViewport.getCamera());

        this.player = gameScreen.player;

        @SuppressWarnings("unchecked")
        List<Exit> exits = (List<Exit>) (Object) this.gameObjects.get(2); // fetches all gameobjects of type exit
        this.exitArrow = new ExitArrow(exits, this.player);
        player.exitArrow = this.exitArrow;

        @SuppressWarnings("unchecked")
        List<Key> keys = (List<Key>) (Object) this.gameObjects.get(5);
        this.keys = keys;

        this.playerScore = 0;

        this.soundHandler = new SoundHandler();

    }

    /**
     * Method which loads the border sprites of the map into the list of tiles.
     * The list contains sprites. The list is later used to draw the level.
     * @param camera
     */
    public List<GameObject> loadBorderTiles(OrthographicCamera camera){
        // used for drawing sprites only until bounds.
        float maxHeight = camera.viewportHeight;
        float maxWidth = camera.viewportWidth;

        List<GameObject> gameObjectList = new ArrayList<>();

        // draw rows
        for (int i = 0; i <= maxWidth/rowBorderTileType.getWidth(); i++) {
            rowBorderTileType.setX(rowBorderTileType.getWidth() * i);

            // load sprite of first row
            Sprite lowerBorderSprite = new Sprite(rowBorderTileType);
            lowerBorderSprite.setY(0);

            // load sprite of last row
            Sprite upperBorderSprite = new Sprite(rowBorderTileType);
            upperBorderSprite.setY(maxHeight - rowBorderTileType.getHeight());


            this.tiles.add(lowerBorderSprite);
            this.tiles.add(upperBorderSprite);

            GameObject low = new BorderWall(lowerBorderSprite.getX(), lowerBorderSprite.getY());
            GameObject high = new BorderWall(upperBorderSprite.getX(), upperBorderSprite.getY());
            gameObjectList.add(low);
            gameObjectList.add(high);
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

            GameObject left = new BorderWall(leftBorderSprite.getX(), leftBorderSprite.getY());
            GameObject right = new BorderWall(rightBorderSprite.getX(), rightBorderSprite.getY());
            gameObjectList.add(left);
            gameObjectList.add(right);
        }


        return gameObjectList;
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
    public void drawGameObjects(SpriteBatch spriteBatch, Player player){
        // draw the borders first
        gameObjects.get(6).forEach(
                go -> {go.draw(spriteBatch, player);}
        );

        gameObjects.forEach(
                (objectType, listOfObjects) -> {
                    listOfObjects.forEach(

                            object -> {
                                if(objectType != 6)
                                    object.draw(spriteBatch, player);
                            }

                    );
                }
        );
        cleanUpBlownUpTraps();
        cleanUpKilledEnemies();
        cleanUpCollectedKeys();
        cleanUpCollectedRandomKill();
        cleanUpCollectedLives();
    }

    /**
     * Collides with temporary player.
     * @param player
     * @return
     */
    public boolean collides(Player player){
        AtomicBoolean collides = new AtomicBoolean(false);

        this.gameObjects.forEach(
                (objectType, gameObjects) -> {
                    gameObjects.forEach(
                            gameObject -> {
                                // shortening to a one-liner does not work.
                                if (gameObject.collide(player)){ // this line calls object.collide
                                    collides.set(true);
                                    if(objectType == 2 && this.player.keysInPosession == this.numberOfKeys)
                                        this.gameScreen.game.goToVictory(this);
                                }
                            }
                    );
                }
        );
        return collides.get();
    }

    public void cleanUpKilledEnemies(){
        List<Integer> indicesOfEnemiesToBeRemoved = new ArrayList<>();
        for (int i = 0; i < this.gameObjects.get(4).size(); i++){
            Enemy enemy = (Enemy) this.gameObjects.get(4).get(i);

            if (enemy.toBeRemoved)
                indicesOfEnemiesToBeRemoved.add(i);
        }
        for (int i : indicesOfEnemiesToBeRemoved){
            this.gameObjects.get(4).remove(i);
            this.soundHandler.enemyDeath.play();
            playerScore += 100;
        }

    }

    public void cleanUpBlownUpTraps(){

        List<Integer> indicesOfTrapsToBeRemoved = new ArrayList<>();
        if(!this.gameObjects.containsKey(3)){
            return;
        }
        // get indices of to be removed traps
       for (int i = 0; i < this.gameObjects.get(3).size(); i++){
           Trap trap = (Trap) this.gameObjects.get(3).get(i);

           if (trap.shouldBeRemoved)
               indicesOfTrapsToBeRemoved.add(i);
       }
       // remove traps at those indices

       for (int i : indicesOfTrapsToBeRemoved){
           this.gameObjects.get(3).remove(i);
       }

    }

    public void cleanUpCollectedKeys(){
        List<Integer> indicesOfKeysToBeCollected = new ArrayList<>();
        for (int i = 0; i < this.gameObjects.get(5).size(); i++){
            Key key = (Key) this.gameObjects.get(5).get(i);

            if (key.shouldBeRemoved)
                indicesOfKeysToBeCollected.add(i);
        }
        for (int i : indicesOfKeysToBeCollected){
            this.soundHandler.keyPickup.play();
            this.player.keysInPosession++;
            this.gameObjects.get(5).remove(i);
        }

    }

    public void cleanUpCollectedRandomKill(){
        List<Integer> indicesOfPowerUpsToBeCollected = new ArrayList<>();
        if(!this.gameObjects.containsKey(7)){
            return;
        }
        for (int i = 0; i < this.gameObjects.get(7).size(); i++){
            RandomKill randomKill = (RandomKill) this.gameObjects.get(7).get(i);

            if (randomKill.shouldBeRemoved)
                indicesOfPowerUpsToBeCollected.add(i);
        }
        for (int i : indicesOfPowerUpsToBeCollected){
            this.player.superPower = (RandomKill) this.gameObjects.get(7).get(i);
            this.gameObjects.get(7).remove(i);
        }
    }

    public void cleanUpCollectedLives(){
        List<Integer> indicesOfLivesToBeCollected = new ArrayList<>();
        if(!this.gameObjects.containsKey(8)){
            return;
        }
        for (int i = 0; i < this.gameObjects.get(8).size(); i++){
            Life life = (Life) this.gameObjects.get(8).get(i);

            if (life.shouldBeRemoved){
                indicesOfLivesToBeCollected.add(i);
                player.heart.restoreHealth(); // increment the life here, maybe this will work
            }

        }
        for (int i : indicesOfLivesToBeCollected){
            this.soundHandler.healthRestore.play();
            this.gameObjects.get(8).remove(i);
        }
    }


    private void setNumberOfKeys(){
        this.numberOfKeys = this.gameObjects.get(5).size();
    }
}
