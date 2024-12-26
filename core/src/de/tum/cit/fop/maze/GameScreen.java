package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.Wall;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

import static de.tum.cit.fop.maze.utilities.LogicHandler.input;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch spriteBatch;

    private final int WORLD_WIDTH = 800;
    private final int WORLD_HEIGHT = 512;
    private final int OBJECT_SCALE = 2;


    private final OrthographicCamera camera;
    private final BitmapFont font;
    private FitViewport fitViewPort;
    float stateTime;

    Player player;
    Array<Sprite> tiles;
    Sprite normalTile;
    Sprite rowBorderTile;
    Sprite columnBorderTile;

    Level level;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();

        // setting up camera and viewport for game screen
        camera = new OrthographicCamera();
        fitViewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        fitViewPort.apply(true);
        camera.setToOrtho(false, fitViewPort.getWorldWidth(), fitViewPort.getWorldHeight());

        // Get the font from the game's skin
        this.font = game.getSkin().getFont("font");

        // normal and border tiles
        this.tiles = new Array<>(2);
        LoaderHelper.loadBackgroundTile(tiles);
        normalTile = tiles.get(0);
        rowBorderTile = tiles.get(1);
        columnBorderTile = new Sprite(rowBorderTile);
        columnBorderTile.rotate90(true);

        // load character unto the game
        player = new Player();


        this.level = new Level("maps/level-1.properties", player);
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        // handling user input logic
        input(game, player, fitViewPort, OBJECT_SCALE, level, delta); // sets current direction to what the user presses

        // drawing unto the screen
        draw();
    }

    // this function takes the input from user and updates the player object with the desired direction.


    // this method receives the direction of the player and rotates the player in the screen accordingly
    public void draw(){
        // setting up the drawing
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        spriteBatch.setProjectionMatrix(fitViewPort.getCamera().combined);


        TextureRegion currentFrame = player.getCurrentAnimation().getKeyFrame(stateTime, true);


        spriteBatch.begin();
        // drawing border and normal tiles
        Level.drawBorderTiles(spriteBatch, rowBorderTile, columnBorderTile, camera);
        Level.drawNormalTiles(spriteBatch, normalTile, camera);
        level.drawGameObjects(spriteBatch);

        // drawing the current player frame
        spriteBatch.draw(currentFrame, player.sprite.getX(), player.sprite.getY(), currentFrame.getRegionWidth() * OBJECT_SCALE, currentFrame.getRegionHeight() * OBJECT_SCALE);


        spriteBatch.end(); // Important to call this after drawing everything
    }

    @Override
    public void resize(int width, int height) {
        fitViewPort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    // Additional methods and logic can be added as needed for the game screen
}
