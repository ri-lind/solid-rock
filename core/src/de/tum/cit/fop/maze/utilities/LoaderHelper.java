package de.tum.cit.fop.maze.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.objects.Enemy;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.Trap;

/**
 * Loads player animations and border and inner tiles.
 */
public class LoaderHelper {


    /**
     * Loads the character animation from the character.png file.
     * Initializes player.sprite as well
     */
    public static void loadCharacterDirectionAnimation(Player player) {
        Texture walkSheet = new Texture(Gdx.files.internal("character.png"));

        // we set the empty direction arrays of the player class to variable names, for instantiating.
        // define player width, height and everything else
        int frameWidth = 16;
        int frameHeight = 32;
        int animationFrames = 4;
        int directionCount = 4;
        // two-dimensional array to save all the array with different direction frames
        Array<Array<TextureRegion>> walkFramesArray = new Array<>(Array.class);

        // Method to get the movement animations from the sprite.
        for (int row = 0; row < directionCount; row++){
            // we instantiate the current direction array
            walkFramesArray.add(new Array<>(TextureRegion.class));
            for (int col = 0; col < animationFrames; col++) {
                // we add the current animation to the direction
                walkFramesArray.get(row).add(new TextureRegion(walkSheet, col * frameWidth, row * frameHeight, frameWidth, frameHeight));
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

        TextureRegion playerFirstAnimation = walkFramesArray.get(0).get(0);


        // instantiate the player sprite here as well.
        player.sprite = new Sprite(playerFirstAnimation);

    }

    public static void loadCharacterAttackAnimations(Player player){
        Texture attackSheet = new Texture(Gdx.files.internal("character.png"));

        // attack sprite size is just different, what the fuck?
        int frameWidth = 16;
        int frameHeight = 32;
        int frameEnd = 104;
        int directions = 4;

        Array<Array<TextureRegion>> attackFramesArray = new Array<>(Array.class);

        // attack double the width of normal movement sprites.
        for (int row = 0; row < directions; row++){
            attackFramesArray.add(new Array<>(TextureRegion.class));
            for (int frame = 8; frame < frameEnd; frame +=  32){
                attackFramesArray.get(row).add(new TextureRegion(attackSheet, frame,
                        (row +4) * frameHeight, frameWidth, frameHeight));
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


    /**
     * Adds 16 x 16 bomb explosion animations to trap.
     * @param trap
     * @param spriteSheetColumn
     * @param spriteSheetRow
     * @param frameWidth
     * @param frameHeight
     * @param spriteSheetFilePath
     */
    public static void loadTrapAnimations(Trap trap, int spriteSheetColumn, int spriteSheetRow,
                                          int frameWidth, int frameHeight, String spriteSheetFilePath) {
        Texture texture = new Texture(Gdx.files.internal(spriteSheetFilePath));

        Array<Sprite> trapFrameArray = new Array<>(Sprite.class);

        for (int column = 0; column < spriteSheetColumn; column++){
            TextureRegion textureRegion = new TextureRegion(texture, column * frameWidth, 0, frameWidth, frameHeight);
            Sprite sprite = new Sprite(textureRegion);
            sprite.setSize(16, 16);
            trapFrameArray.add(sprite);
        }
        trap.animations = new Animation<>(0.1f, trapFrameArray);
    }
}