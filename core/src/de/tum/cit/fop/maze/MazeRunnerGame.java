package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.objects.Player;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;

    Player player;

    Texture testBackground;
    // 0 - normal Tile, 1 - borderTile
    Array<Sprite> tiles;
    // define dimensions of world and viewport, since they have to be the same

    /**
     * Constructor for MazeRunnerGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
   }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin, what is this for?

        this.testBackground = new Texture(Gdx.files.internal("test_background.png"));

        // normal and border tiles
        this.tiles = new Array<>(2);
        this.loadBackgroundTile();

        // load character unto the game
        player = new Player();
        this.loadCharacterAnimation(); // Load character animation

        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    private void loadCharacterAnimation() {
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
        player.directionAnimations.add(new Animation<>(0.1f, walkFramesArray.get(0)));
        player.directionAnimations.add(new Animation<>(0.1f, walkFramesArray.get(1)));
        player.directionAnimations.add(new Animation<>(0.1f, walkFramesArray.get(2)));
        player.directionAnimations.add(new Animation<>(0.1f, walkFramesArray.get(3)));

    }

    // not used, until the camera viewport problems are sorted out.
    private void loadBackgroundTile(){
        // frame width and height: Are they correct?
        int frameWidth = 16;
        int frameHeight = 16;
        Texture tileSheet = new Texture(Gdx.files.internal("basictiles.png"));
        loadNormalBackgroundTile(tileSheet, frameWidth, frameHeight);
        loadBackgroundBorderTile(tileSheet, frameWidth, frameHeight);
    }

    private void loadNormalBackgroundTile(Texture tileSheet, int frameWidth, int frameHeight){
        // I believe this fetches the wooden tile, third row first column
        TextureRegion tile = new TextureRegion(tileSheet, frameWidth * 1, frameHeight * 1, frameWidth, frameHeight);
        // conversion to sprite
        Sprite tileSprite = new Sprite(tile);
        tiles.add(tileSprite);
    }

    private void loadBackgroundBorderTile(Texture tileSheet, int frameWidth, int frameHeight){
        // logic for border
        TextureRegion borderTile = new TextureRegion(tileSheet, 4 * frameWidth, 0, frameWidth, frameHeight);
        Sprite borderTileSprite = new Sprite(borderTile);
        tiles.add(borderTileSprite);
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods

    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
