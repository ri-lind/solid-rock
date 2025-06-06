package de.tum.cit.fop.maze.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.EntryPoint;
import de.tum.cit.fop.maze.objects.Fire;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.enemy.Enemy;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.obstacles.BookOfRandomSpawn;
import de.tum.cit.fop.maze.objects.obstacles.Trap;
import de.tum.cit.fop.maze.utilities.level.LevelHandler;

import java.util.List;
import java.util.Map;

/**
 * Loads player animations and border and inner tiles.
 */
public class LoaderHelper {

    OrthographicCamera camera;

    public LoaderHelper(OrthographicCamera camera){
        this.camera = camera;
    }
    /**
     * Loads the character animation from the character.png file.
     * Initializes player.sprite as well
     */
    public static void loadCharacterDirectionAnimation(Player player) {
        Texture walkSheet = new Texture(Gdx.files.internal("transparent_background/character.png"));

        // we set the empty direction arrays of the player class to variable names, for instantiating.
        // define player width, height and everything else
        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;
        int directionCount = 4;
        // two-dimensional array to save all the array with different direction frames
        Array<Array<Sprite>> walkFramesArray = new Array<>(Array.class);

        // Method to get the movement animations from the sprite.
        for (int row = 0; row < directionCount; row++){
            // we instantiate the current direction array
            walkFramesArray.add(new Array<>(TextureRegion.class));
            for (int col = 0; col < animationFrames; col++) {
                // we add the current animation to the direction
                TextureRegion textureRegion = new TextureRegion(walkSheet, col * frameWidth + 2, row * frameHeight+5, frameWidth-3, frameHeight-9);
                Sprite sprite = new Sprite(textureRegion);
                walkFramesArray.get(row).add(sprite);
            }
        }
        // the movements arrays for the various directions are added to the player
        player.animations.put("down", new Animation<>(0.1f, walkFramesArray.get(0)));
        player.animations.put("right", new Animation<>(0.1f, walkFramesArray.get(1)));
        player.animations.put("up", new Animation<>(0.1f, walkFramesArray.get(2)));
        player.animations.put("left", new Animation<>(0.1f, walkFramesArray.get(3)));

        player.animations.put("down-standing", new Animation<>(0.1f, walkFramesArray.get(0).get(0)));
        player.animations.put("right-standing", new Animation<>(0.1f, walkFramesArray.get(1).get(0)));
        player.animations.put("up-standing", new Animation<>(0.1f, walkFramesArray.get(2).get(0)));
        player.animations.put("left-standing", new Animation<>(0.1f, walkFramesArray.get(3).get(0)));

        Sprite playerFirstAnimation = walkFramesArray.get(0).get(0);


        // instantiate the player sprite here as well.
        player.sprite = new Sprite(playerFirstAnimation);

    }

    public static void loadCharacterAttackAnimations(Player player){
        Texture attackSheet = new Texture(Gdx.files.internal("transparent_background/character.png"));

        // attack sprite size is just different, what the fuck?
        int frameWidth = 16;
        int frameHeight = 32;
        int frameEnd = 104;
        int directions = 4;

        Array<Array<Sprite>> attackFramesArray = new Array<>(Array.class);

        // attack double the width of normal movement sprites.
        for (int row = 0; row < directions; row++){
            attackFramesArray.add(new Array<>(TextureRegion.class));
            for (int frame = 8; frame < frameEnd; frame +=  32){
                TextureRegion textureRegion = new TextureRegion(attackSheet, frame,
                        (row +4) * frameHeight+5, frameWidth, frameHeight-9);
                Sprite sprite = new Sprite(textureRegion);
                attackFramesArray.get(row).add(sprite);
            }
        }

        player.animations.put("down-attacking", new Animation<>(0.1f, attackFramesArray.get(0)));
        player.animations.put("up-attacking", new Animation<>(0.1f, attackFramesArray.get(1)));
        player.animations.put("right-attacking", new Animation<>(0.1f, attackFramesArray.get(2)));
        player.animations.put("left-attacking", new Animation<>(0.1f, attackFramesArray.get(3)));
    }

    public static Sprite loadNormalBackgroundTile( ){
        int frameWidth = 16;
        int frameHeight = 16;
        Texture texture = new Texture(Gdx.files.internal("basictiles.png"));
        // I believe this fetches the wooden tile, third row first column
        TextureRegion tile = new TextureRegion(texture, frameWidth * 1, frameHeight * 1, frameWidth, frameHeight);
        // conversion to sprite
        return new Sprite(tile);
    }

    public static Sprite loadBackgroundBorderTile(){
        int frameWidth = 16;
        int frameHeight = 16;
        Texture texture = new Texture(Gdx.files.internal("basictiles.png"));
        // logic for border
        TextureRegion borderTile = new TextureRegion(texture, 4 * frameWidth, 0, frameWidth, frameHeight);
        return new Sprite(borderTile);

    }

    public static void loadEnemyDirectionAnimations(Enemy enemy){

        Texture texture = new Texture(Gdx.files.internal(Enemy.spriteSheetFilePath));

        int frameWidth = 16;
        int frameHeight = 16;
        int animationFrames = 3;
        int directionCount = 4;

        Array<Array<Sprite>> walkFramesArray = new Array<>(Array.class);

        for (int row = 0; row < directionCount; row++){
            walkFramesArray.add(new Array<>(Sprite.class));
            for (int column = 9; column < 9 +animationFrames; column++){
                TextureRegion region = new TextureRegion(texture, column * frameWidth, row * frameHeight, frameWidth, frameHeight);
                Sprite sprite = new Sprite(region);
                walkFramesArray.get(row).add(sprite);
            }
        }

        enemy.animationMap.put("down", new Animation<>(0.1f, walkFramesArray.get(0)));
        enemy.animationMap.put("left", new Animation<>(0.1f, walkFramesArray.get(1)));
        enemy.animationMap.put("right", new Animation<>(0.1f, walkFramesArray.get(2)));
        enemy.animationMap.put("up", new Animation<>(0.1f, walkFramesArray.get(3)));
    }

    public static void loadTrapAnimations(Trap trap, int frameWidth, int frameHeight, String spriteSheetFilePath, float x, float y) {


        int trapWidth = 16;
        int trapHeight = 16;
        int rows = 2;
        int columns = 13;
        Texture texture = new Texture(Gdx.files.internal(spriteSheetFilePath));

        Array<Sprite> trapFrameArray = new Array<>(Sprite.class);

        for (int column = 0; column < columns; column++){
            for (int row = 0; row < rows; row++) { // row should be 13
                if(column < 9 || column == columns-1) { // load the lower part of the animation
                    TextureRegion textureRegion = new TextureRegion(texture, column * frameWidth, frameHeight/2, frameWidth, frameHeight/2);
                    Sprite oneOfTheFirst8Sprites = new Sprite(textureRegion);
                    oneOfTheFirst8Sprites.setX(x);
                    oneOfTheFirst8Sprites.setY(y);
                    trapFrameArray.add(oneOfTheFirst8Sprites);
                } else if (column == 9){ // animate both upper and lower part for highest point of explosion
                    TextureRegion textureRegion = new TextureRegion(texture, column * frameWidth, 0, frameWidth, frameHeight);
                    Sprite theHeightOfTheExplosion = new Sprite(textureRegion);
                    theHeightOfTheExplosion.setX(x);
                    theHeightOfTheExplosion.setY(y);
                    trapFrameArray.add(theHeightOfTheExplosion);
                } else { // animate upper part only
                    TextureRegion textureRegion = new TextureRegion(texture, column * frameWidth, 0, frameWidth, frameHeight/2);
                    Sprite explosionAftermath = new Sprite(textureRegion);
                    explosionAftermath.setX(x);
                    explosionAftermath.setY(y);
                    explosionAftermath.translateY(explosionAftermath.getHeight());
                    trapFrameArray.add(explosionAftermath);
                }

            }

        }

        trapFrameArray.forEach( (frame) -> {frame.setSize(trapWidth, trapHeight);});
        trap.animations = new Animation<>(0.1f, trapFrameArray);
    }


    public static PlayerSpawnCoordinates getPlayerSpawnCoordinates(String filename, FitViewport fitViewport){
        String mapContent = LevelHandler.readMapFromFile(filename);
        Map<Integer, List<GameObject>> gameObjects = LevelHandler.createGameObjects(mapContent, fitViewport);

        EntryPoint entryPoint = (EntryPoint) gameObjects.get(1).get(0);

        float playerX = entryPoint.sprite.getX() + entryPoint.sprite.getWidth() + 5;
        float playerY = entryPoint.sprite.getY() + entryPoint.sprite.getHeight();


        return new PlayerSpawnCoordinates(playerX, playerY);
    }


    public static void loadBookOfRandomSpawnAnimations(BookOfRandomSpawn book, int frameWidth, int frameHeight,
                                                  String spriteSheetFilePath, float x, float y){
    Array<Sprite> bookAnimation = new Array<>(Sprite.class);
    Texture texture = new Texture(spriteSheetFilePath);



    for (int i = 15; i <= 19; i++){
        TextureRegion textureRegion = new TextureRegion(texture, i * frameWidth, 0, frameWidth, frameHeight);

        Sprite sprite = new Sprite(textureRegion);
        sprite.setPosition(x, y);
        sprite.setSize(16, 16);

        bookAnimation.add(sprite);
    }

    // now it is time to add the smoke, should be about three frames

    // then we add the enemy, or random superpower

    book.animations = new Animation<>(0.5f, bookAnimation);
    }


    public static void loadFireAnimations(Fire fire, int frameWidth, int frameHeight,
                                           String spriteFileSheetPath, float x, float y){
        Array<Sprite> fireSpritesArray = new Array<>(Sprite.class);

        Texture texture = new Texture(Gdx.files.internal(spriteFileSheetPath));

        for(int i = 4; i < 11; i++){
            TextureRegion textureRegion = new TextureRegion(texture, i * frameWidth, 3 * frameHeight, frameWidth, frameHeight);

            Sprite sprite = new Sprite(textureRegion);
            sprite.setPosition(x, y);
            sprite.setSize(16, 16);

            fireSpritesArray.add(sprite);
        }

        fire.animations = new Animation<>(0.3f, fireSpritesArray);
    }
}