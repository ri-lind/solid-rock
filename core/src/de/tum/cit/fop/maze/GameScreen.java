package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Player;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch spriteBatch;

    private final OrthographicCamera camera;
    private final BitmapFont font;

    private FitViewport fitViewPort;

    float stateTime;

    Player player;

    Sprite normalTile;
    Sprite rowBorderTile;
    Sprite columnBorderTile;

    private final int OBJECT_SCALE = 2;
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        fitViewPort = new FitViewport(800, 512, camera);
        fitViewPort.apply(true);
        camera.setToOrtho(false, fitViewPort.getWorldWidth(), fitViewPort.getWorldHeight());

        // Get the font from the game's skin
        this.font = game.getSkin().getFont("font");
        this.player = game.player;

        // draw at the beginning of the game, so that the rotation of drawBorderTiles is not called at every frame.
        normalTile = game.tiles.get(0);
        rowBorderTile = game.tiles.get(1);
        columnBorderTile = new Sprite(rowBorderTile);
        columnBorderTile.rotate90(true);

        System.out.println(normalTile.getWidth());
        System.out.println(rowBorderTile.getWidth());
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        // method that takes input and aligns player direction.
        input(player); // sets current direction to what the user presses

        draw(); // makes the magic happen
    }

    // this function takes the input from user and updates the player object with the desired direction.
    public void input(Player player) {

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.currentDirection = "UP";
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.currentDirection = "DOWN";
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.currentDirection = "LEFT";
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.currentDirection = "RIGHT";
        } else {
            if("down".contains(player.currentDirection.toLowerCase())){
                player.currentDirection = "down-standing";
            }else if("up".contains(player.currentDirection.toLowerCase())){
                player.currentDirection = "up-standing";
            } else if("left".contains(player.currentDirection.toLowerCase())){
                player.currentDirection = "left-standing";
            } else if("right".contains(player.currentDirection.toLowerCase())){
                player.currentDirection = "right-standing";
            }
        }

        // logic to check if player is going out of bounds.
        Player temporaryPlayer = new Player(game.player.x, game.player.y, game.player.currentDirection);
        temporaryPlayer.calculateNextMove();

        // key frame foes not matter, I believe...
        if (temporaryPlayer.x + player.width * OBJECT_SCALE >= fitViewPort.getWorldWidth() ||
            temporaryPlayer.x < 0){

        } else if (temporaryPlayer.y + player.height >= fitViewPort.getWorldHeight() ||
                temporaryPlayer.y < 0 ) {
        }else {
            game.player.calculateNextMove(); // move the player along the current direction

        }


        // move player into required direction here.
    }
    public void logic(){
    }
    // this method receives the direction of the player and rotates the player in the screen accordingly
    public void draw(){

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // Set up and begin drawing with the sprite batch
        spriteBatch.setProjectionMatrix(fitViewPort.getCamera().combined);

        TextureRegion currentFrame = player.getCurrentAnimation().getKeyFrame(stateTime, true);


        spriteBatch.begin(); // Important to call this before drawing anything


        Level.drawBorderTiles(spriteBatch, rowBorderTile, columnBorderTile, camera);
        Level.drawNormalTiles(spriteBatch, normalTile, camera);
        // draw the player
        spriteBatch.draw(currentFrame, player.x, player.y, currentFrame.getRegionWidth() * OBJECT_SCALE, currentFrame.getRegionHeight() * OBJECT_SCALE);
        //draw the background
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
    }

    // Additional methods and logic can be added as needed for the game screen
}
