package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.*;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.collectables.Life;
import de.tum.cit.fop.maze.objects.collectables.RandomKill;
import de.tum.cit.fop.maze.objects.enemy.Breadcrumb;
import de.tum.cit.fop.maze.objects.enemy.Enemy;
import de.tum.cit.fop.maze.objects.obstacles.Trap;
import de.tum.cit.fop.maze.utilities.LogicHandler;
import de.tum.cit.fop.maze.utilities.level.LevelHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    public final MazeRunnerGame game;
    private final SpriteBatch gameSpriteBatch;
    private final Skin skin;

    private final int WORLD_WIDTH = 800;
    private final int WORLD_HEIGHT = 512;


    private final BitmapFont font;
    private final FitViewport fitViewPort;
    float stateTime;

    public Player player;
    Level level;

    public Music levelMusic;


    public ShapeRenderer shapeRenderer;
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, int level_number) {

        this.levelMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/level.mp3"));
        this.levelMusic.setVolume(0.05f);
        this.levelMusic.play();
        this.game = game;
        this.gameSpriteBatch = game.getSpriteBatch();
        this.skin = game.getSkin();

        // setting up camera and viewport for game screen
        OrthographicCamera camera = new OrthographicCamera();
        fitViewPort = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        fitViewPort.apply(true);
        camera.setToOrtho(false, fitViewPort.getWorldWidth(), fitViewPort.getWorldHeight());

        System.out.println("Camera Viewport Height" + camera.viewportHeight);
        System.out.println("World Height" + fitViewPort.getWorldHeight());
        // Get the font from the game's skin
        this.font = game.getSkin().getFont("font");

        String mapFileName = String.format("maps/level-%s.properties", level_number);

        this.player = new Player(mapFileName, this.fitViewPort);
        this.level = new Level(String.format("maps/level-%s.properties", level_number), this.fitViewPort, this);


        this.shapeRenderer = new ShapeRenderer();
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu(true);
        }

        if (Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get where the touch happened on screen
            level.fitViewport.unproject(touchPos); // Convert the units to the world units of the viewport

            this.level.mapCreator.spawnAtGivenCoordinates(touchPos.x, touchPos.y);
        }

        // spawn wall at the current coordinates
        if(Gdx.input.isKeyJustPressed(Input.Keys.V)){
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            level.fitViewport.unproject(touchPos);
            GameObject gameObject = this.level.mapCreator.getWallInTheVicinity(touchPos.x, touchPos.y);
            AtomicReference<GameObject> objectToBeRemoved = new AtomicReference<>();
            this.level.gameObjects.forEach(
                    ((objectType, gameObjects) -> {
                        gameObjects.forEach(go -> {
                            if(go.sprite.getX() == gameObject.sprite.getX() && go.sprite.getY() == gameObject.sprite.getY()){
                                objectToBeRemoved.set(go);

                            }
                        });
                    })
            );

            this.level.gameObjects.get(0).remove(objectToBeRemoved.get());
            this.level.gameObjects.get(6).remove(objectToBeRemoved.get());

        }

        // spawn enemy at current coordinates
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            Vector2 enemyCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            this.level.fitViewport.unproject(enemyCoordinates);

            Enemy enemy = new Enemy(enemyCoordinates.x, enemyCoordinates.y);

            List<GameObject> enemies = this.level.gameObjects.computeIfAbsent(4, k-> new ArrayList<>());
            enemies.add(enemy);
        }

        // spawn key on click
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
            Vector2 keyCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            this.level.fitViewport.unproject(keyCoordinates);

            List<GameObject> keysAlreadyPresent = this.level.gameObjects.get(5) ;

            Key key = new Key(keyCoordinates.x, keyCoordinates.y);
            if(keysAlreadyPresent == null){
                keysAlreadyPresent = new ArrayList<>();
            }
           keysAlreadyPresent.add(key);
            this.level.numberOfKeys++;
        }

        // spawn trap on click
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
            Vector2 trapCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            this.level.fitViewport.unproject(trapCoordinates);

            Trap trap = new Trap(trapCoordinates.x, trapCoordinates.y);
            this.level.gameObjects.get(3).add(trap);
        }

        // spawn collectable heart on click
        if(Gdx.input.isKeyJustPressed(Input.Keys.H)){
            Vector2 heartCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            this.level.fitViewport.unproject(heartCoordinates);

            Life life = new Life(heartCoordinates.x, heartCoordinates.y);

            List<GameObject> livesAlreadyPresent = this.level.gameObjects.computeIfAbsent(8, k -> new ArrayList<>());

            livesAlreadyPresent.add(life);
        }

        // spawn random kill on click
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            Vector2 randomKillCoordinates = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            this.level.fitViewport.unproject(randomKillCoordinates);

            RandomKill randomKill = new RandomKill(randomKillCoordinates.x, randomKillCoordinates.y);

            List<GameObject> randomKillsAlreadyPresent = this.level.gameObjects.computeIfAbsent(7, k -> new ArrayList<>());

            randomKillsAlreadyPresent.add(randomKill);
        }


        // save current level to a file.
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            // logic to take all current game objects and write the output to a properties file.
            String propertiesFileString = LevelHandler.convertMapToString(this.level.gameObjects);
            FileHandle fileHandle = Gdx.files.local("manually-created-level.properties");
            fileHandle.writeString(propertiesFileString, false);
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
        shapeRenderer.setProjectionMatrix(fitViewPort.getCamera().combined); // added for the circle drawing of the mouse click.

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
        font.getData().setScale(1f);
        gameSpriteBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Vector2 pointerPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        level.fitViewport.unproject(pointerPosition);
        shapeRenderer.circle(pointerPosition.x, pointerPosition.y, 30f);
        shapeRenderer.end();
        if (player.heart.remaining_life < 1){
            game.goToGameOver(this.level);
        }
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
        this.levelMusic.stop();
        //gameSpriteBatch.dispose(); //does the game sprite batch need to be disposed?
    }

    // Additional methods and logic can be added as needed for the game screen
}
