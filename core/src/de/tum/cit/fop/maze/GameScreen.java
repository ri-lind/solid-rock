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
        // the following method takes the player and rotates him according to the input.
        // has been quite the bother to implement to be honest. Scary!
        input(player);
        Animation<TextureRegion> playerDirectionAnimation;
        if(Objects.equals(player.currentDirection, "UP")){
            playerDirectionAnimation = player.characterUpAnimation;
            player.y += 1;
        } else if(Objects.equals(player.currentDirection, "DOWN")){
            playerDirectionAnimation = player.characterDownAnimation;
            player.y -= 1;
        } else if(Objects.equals(player.currentDirection, "LEFT")){
            playerDirectionAnimation = player.characterLeftAnimation;
            player.x -= 1;
        } else {
            playerDirectionAnimation = player.characterRightAnimation;
            player.x += 1;
        }
        logic();
        draw(playerDirectionAnimation);
    }

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
    public void draw(Animation<TextureRegion> playerDirectionAnimation){
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        SpriteBatch gameSpriteBatch = game.getSpriteBatch();

        // Set up and begin drawing with the sprite batch
        gameSpriteBatch.setProjectionMatrix(camera.combined);

        Player player = game.player;
        float deltaTime = Gdx.graphics.getDeltaTime();
        stateTime += deltaTime;
        TextureRegion currentFrame = playerDirectionAnimation.getKeyFrame(stateTime, true);

        gameSpriteBatch.begin(); // Important to call this before drawing anything


        this.spriteBatch.draw(currentFrame, player.x, player.y); // change the direction
        gameSpriteBatch.end(); // Important to call this after drawing everything
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
