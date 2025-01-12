package de.tum.cit.fop.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.fop.maze.objects.Level;

import java.util.concurrent.atomic.AtomicReference;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {

    private final Stage stage;

    public Music menuMusic;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    private MenuScreen(MazeRunnerGame game) {

        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
        this.menuMusic.play();
        this.menuMusic.setVolume(0.2f);
        this.menuMusic.setLooping(true);
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.setFillParent(true);
        stage.addActor(verticalGroup);

        verticalGroup.addActor(new Label("Solid Rock", game.getSkin(), "title"));
        verticalGroup.addActor(new Label(" ", game.getSkin(), "title"));


        // Add a label as a title
        Actor placeholder1 = new Label("", game.getSkin());
        Actor placeholder2 = new Label("", game.getSkin());
        Actor placeholder3 = new Label("", game.getSkin());
        Actor placeholder4 = new Label("", game.getSkin());

        TextButton level1Button = new TextButton("Level 1", game.getSkin());
        verticalGroup.addActor(level1Button);//.padBottom(50).width(300).row();
        verticalGroup.addActor(placeholder1);
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(1);
            }
        });
        TextButton level2Button = new TextButton("Level 2", game.getSkin());
        verticalGroup.addActor(level2Button);
        verticalGroup.addActor(placeholder2);
        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(2);
            }
        });
        TextButton level3Button = new TextButton("Level 3", game.getSkin());
        verticalGroup.addActor(level3Button);
        verticalGroup.addActor(placeholder3);
        level3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(3);
            }
        });
        TextButton level4Button = new TextButton("Level 4", game.getSkin());
        verticalGroup.addActor(level4Button);
        verticalGroup.addActor(placeholder4);
        level4Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(4);
            }
        });
        TextButton level5Button = new TextButton("Level 5", game.getSkin());
        verticalGroup.addActor(level5Button);
        level5Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(5);
            }
        });


    }

    public MenuScreen(MazeRunnerGame game, boolean paused){
        this(game);
        Music menuMusic = this.menuMusic;
        if (paused) {
            AtomicReference<VerticalGroup> verticalGroupAtomicReference = new AtomicReference<>(new VerticalGroup());
            stage.getActors().forEach((actor -> {
                if (actor.getClass() == VerticalGroup.class){
                    verticalGroupAtomicReference.set((VerticalGroup) actor);
                }
            }));
            VerticalGroup verticalGroup = verticalGroupAtomicReference.get();
            TextButton goBackToLevel = new TextButton("Resume", game.getSkin());
            Actor placeholder = new Label("", game.getSkin());
            verticalGroup.addActorAt(2, goBackToLevel);
            verticalGroup.addActorAt(3, placeholder);
            goBackToLevel.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    menuMusic.stop();
                    game.goToGame();
                }
            });
        }

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
