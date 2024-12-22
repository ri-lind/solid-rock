package de.tum.cit.fop.maze.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.objects.Player;

public class LoaderHelper {


    /**
     * Loads the character animation from the character.png file.
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
        player.directionAnimations.put("down", new Animation<>(0.1f, walkFramesArray.get(0)));
        player.directionAnimations.put("right", new Animation<>(0.1f, walkFramesArray.get(1)));
        player.directionAnimations.put("up", new Animation<>(0.1f, walkFramesArray.get(2)));
        player.directionAnimations.put("left", new Animation<>(0.1f, walkFramesArray.get(3)));

        player.directionAnimations.put("down-standing", new Animation<>(0.1f, walkFramesArray.get(0).get(0)));
        player.directionAnimations.put("right-standing", new Animation<>(0.1f, walkFramesArray.get(1).get(0)));
        player.directionAnimations.put("up-standing", new Animation<>(0.1f, walkFramesArray.get(2).get(0)));
        player.directionAnimations.put("left-standing", new Animation<>(0.1f, walkFramesArray.get(3).get(0)));


        player.height = walkFramesArray.get(0).get(0).getRegionHeight();
        player.width = walkFramesArray.get(0).get(0).getRegionWidth();

    }

    public static void loadCharacterAttackAnimations(Player player){
        Texture attackSheet = new Texture(Gdx.files.internal("character.png"));

        // attack sprite size is just different, what the fuck?
        int frameWidth = 32;
        int frameHeight = 32;
        int animationFrames = 4;
        int directions = 4;

        Array<Array<TextureRegion>> attackFramesArray = new Array<>(Array.class);

        for (int row = 0; row < directions; row++){
            attackFramesArray.add(new Array<>(TextureRegion.class));
            for (int col = 0; col < animationFrames; col++){
                attackFramesArray.get(row).add(new TextureRegion(attackSheet, (col+ 4) * frameWidth,
                        (row +4) * frameHeight, frameWidth, frameHeight));
            }
        }

        //player.attackAnimations.put("", );
        /*
        player.attackAnimations.add(new Animation<>(0.1f, attackFramesArray.get(0)));
        player.attackAnimations.add(new Animation<>(0.1f, attackFramesArray.get(1)));
        player.attackAnimations.add(new Animation<>(0.1f, attackFramesArray.get(2)));
        player.attackAnimations.add(new Animation<>(0.1f, attackFramesArray.get(3)));
        */
    }


    // tile loading classes

    // not used, until the camera viewport problems are sorted out.
    public static void loadBackgroundTile(Array<Sprite> tiles){
        // frame width and height: Are they correct?
        int frameWidth = 16;
        int frameHeight = 16;
        Texture tileSheet = new Texture(Gdx.files.internal("basictiles.png"));
        loadNormalBackgroundTile(tileSheet, frameWidth, frameHeight, tiles);
        loadBackgroundBorderTile(tileSheet, frameWidth, frameHeight, tiles);
    }

    private static void loadNormalBackgroundTile(Texture tileSheet, int frameWidth, int frameHeight, Array<Sprite> tiles){
        // I believe this fetches the wooden tile, third row first column
        TextureRegion tile = new TextureRegion(tileSheet, frameWidth * 1, frameHeight * 1, frameWidth, frameHeight);
        // conversion to sprite
        Sprite tileSprite = new Sprite(tile);
        tiles.add(tileSprite);
    }

    private static void loadBackgroundBorderTile(Texture tileSheet, int frameWidth, int frameHeight, Array<Sprite> tiles){
        // logic for border
        TextureRegion borderTile = new TextureRegion(tileSheet, 4 * frameWidth, 0, frameWidth, frameHeight);
        Sprite borderTileSprite = new Sprite(borderTile);
        tiles.add(borderTileSprite);
    }
}
