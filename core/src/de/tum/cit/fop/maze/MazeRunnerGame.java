package de.tum.cit.fop.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.fop.maze.objects.Level;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private VictoryScreen victoryScreen;
    private GameOverScreen gameOverScreen;

    // Sprite Batch for rendering
    private SpriteBatch spriteBatch;

    // UI Skin
    private Skin skin;


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
        this.goToMenu(false); // Navigate to the menu screen
    }

    /**
     * Switches to the menu screen.
     */

    /**
     * Switches to the menu screen.
     */
    public void goToMenu(boolean paused) {
        MenuScreen menuScreen = new MenuScreen(this, paused);

        this.setScreen(menuScreen); // Set the current screen to MenuScreen
        this.menuScreen = menuScreen;
        if (gameScreen != null) {
            gameScreen.levelMusic.stop();
            //gameScreen.dispose(); // Dispose the game screen if it exists
            //gameScreen = null;
        }
        if (victoryScreen != null){
            victoryScreen.winningSound.stop();
            //victoryScreen.dispose();
            //victoryScreen = null;
        }
    }

    /**
     * Switches to the game screen.
     */
    public void goToGame(int level_number) {
        GameScreen gameScreen = new GameScreen(this, level_number);
        this.gameScreen = gameScreen;
        this.setScreen(gameScreen); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.menuMusic.stop();
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    public void goToGame() {
        this.setScreen(this.gameScreen); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.menuMusic.stop();
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    public void goToVictory(Level level) {
        VictoryScreen victoryScreen = new VictoryScreen(this, level);
        this.victoryScreen = victoryScreen;
        this.setScreen(victoryScreen);
        if(gameScreen != null){
            gameScreen.levelMusic.stop();
            //gameScreen.dispose(); // thi
            gameScreen = null;
        }
    }

    public void goToGameOver(Level level) {
        GameOverScreen gameOverScreen = new GameOverScreen(this, level);
        this.setScreen(gameOverScreen);
        if(gameScreen != null){
            gameScreen.levelMusic.stop();
            //gameScreen.dispose();
            gameScreen = null;
        }
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
