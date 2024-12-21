package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.Player;

import java.util.Objects;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private FitViewport viewport;

    float stateTime;

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
        camera.setToOrtho(false);
        camera.zoom = 0.25f;

        // create viewport to set world boundaries and not let character move more than it.
        viewport = new FitViewport(8, 5, camera);

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        Player player = game.player;
        // method that takes input and aligns player direction.
        input(player); // sets current direction to what the user presses

        draw(); // makes the magic happen
    }

    // this function takes the input from user and updates the player object with the desired direction.
    public void input(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.currentDirection = "UP";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            player.currentDirection = "DOWN";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player.currentDirection = "LEFT";
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player.currentDirection = "RIGHT";
        }
    }
    public void logic(){

    }
    // this method receives the direction of the player and rotates the player in the screen accordingly
    public void draw(){
        Player player = game.player;
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        // check for the coordinates of the player, so that they are not bigger than the worldview.
        // Set up and begin drawing with the sprite batch
        spriteBatch.setProjectionMatrix(camera.combined);
        float deltaTime = Gdx.graphics.getDeltaTime();

        // I create a player with the same x and y, for world boundary calculation purposes.

        Player temporaryPlayer = new Player(player.x, player.y, player.currentDirection);
        temporaryPlayer.calculateNextMove(); // we move the temp player in the direction we would like to go.
        player.calculateNextMove();
        // the world width and height
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        stateTime += deltaTime;
        TextureRegion currentFrame = player.getCurrentAnimation().getKeyFrame(stateTime, true);

        spriteBatch.begin(); // Important to call this before drawing anything

        /*
        if(temporaryPlayer.x + currentFrame.getRegionWidth() <= worldWidth  & temporaryPlayer.x >= 0){
            if (temporaryPlayer.y + currentFrame.getRegionHeight() <= worldHeight & temporaryPlayer.y >=0){
                this.spriteBatch.draw(currentFrame, temporaryPlayer.x, temporaryPlayer.y);
            }
        }else {
            this.spriteBatch.draw(currentFrame, player.x, player.y); // change the direction and move player

        }*/
        this.spriteBatch.draw(currentFrame, player.x, player.y);
        spriteBatch.end(); // Important to call this after drawing everything
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
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
