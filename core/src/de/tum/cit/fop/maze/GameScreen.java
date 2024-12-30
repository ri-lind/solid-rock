package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.*;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.enemy.Breadcrumb;
import de.tum.cit.fop.maze.utilities.LogicHandler;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final SpriteBatch gameSpriteBatch;
    private final Skin skin;

    private final int WORLD_WIDTH = 800;
    private final int WORLD_HEIGHT = 512;


    private final BitmapFont font;
    private final FitViewport fitViewPort;
    float stateTime;

    public Player player;
    Level level;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        this.gameSpriteBatch = game.getSpriteBatch();
        this.skin = game.getSkin();

        // setting up camera and viewport for game screen
        OrthographicCamera camera = new OrthographicCamera();
        fitViewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        fitViewPort.apply(true);
        camera.setToOrtho(false, fitViewPort.getWorldWidth(), fitViewPort.getWorldHeight());

        // Get the font from the game's skin
        this.font = game.getSkin().getFont("font");

        this.player = new Player();

        this.level = new Level("maps/level-1.properties", camera, this);
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }
        // handling user input logic
        LogicHandler.input(game, player, fitViewPort, level, delta); // sets current direction to what the user presses

        // drawing unto the screen
        draw();
    }

    /**
     * Logic for drawing the game screen.
     */
    public void draw(){
        // setting up the drawing
        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
        gameSpriteBatch.setProjectionMatrix(fitViewPort.getCamera().combined);

        // position player sprite
        Sprite currentPlayerFrame = player.getCurrentAnimation().getKeyFrame(stateTime, true);
        currentPlayerFrame.setPosition(player.sprite.getX(), player.sprite.getY());
        // draw the player and the game objects
        gameSpriteBatch.begin();

        level.tiles.forEach(
                (sprite) -> {
                    sprite.draw(gameSpriteBatch);
                }
        );

        level.drawGameObjects(gameSpriteBatch, this.player);

        currentPlayerFrame.draw(gameSpriteBatch);

        player.heart.sprite.draw(gameSpriteBatch);
        level.exitArrow.sprite.draw(gameSpriteBatch);

        // draw player breadcrumbs
        Breadcrumb.allPlayerBreadCrumbs.forEach(breadcrumb ->
        {
            if(breadcrumb.sprite != null){
                breadcrumb.sprite.draw(gameSpriteBatch);
            }
            });
        Breadcrumb.removeOutDatedBreadcrumbs();

        BitmapFont font = skin.getFont("font");

        font.getData().setScale(0.4f);
        font.draw(gameSpriteBatch, player.keysInPosession+ "", currentPlayerFrame.getX() + 5, currentPlayerFrame.getY()-8);

        gameSpriteBatch.end();


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
        gameSpriteBatch.dispose();
    }

    // Additional methods and logic can be added as needed for the game screen
}
