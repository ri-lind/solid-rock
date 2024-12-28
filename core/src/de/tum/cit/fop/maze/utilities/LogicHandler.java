package de.tum.cit.fop.maze.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.MazeRunnerGame;

/**
 * Class which handles the user input with respect to the player character.
 *
 * Logic is currently very fragile.
 */
public class LogicHandler {

    public static void input(MazeRunnerGame game, Player player,
                             FitViewport fitViewport, Level level, float delta) {

        // code to keep track of attack animation
        if(player.currentDirection.toLowerCase().contains("attacking")){
            player.attackFramesCounter++;
            if (player.attackFramesCounter > 10){
                player.currentDirection = player.currentDirection.replace("attacking","standing");
                player.attackFramesCounter = 0;
            }
        }

        // logic for initiating attack, lasts 10 (frames ?)
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if (player.currentDirection.toLowerCase().contains("down")){
                player.currentDirection = "down-attacking";
            } else if (player.currentDirection.toLowerCase().contains("up")){
                player.currentDirection = "up-attacking";
            } else if (player.currentDirection.toLowerCase().contains("left")){
                player.currentDirection = "left-attacking";
            } else if (player.currentDirection.toLowerCase().contains("right")){
                player.currentDirection = "right-attacking";
            }
            return;
        }

        // movement animation
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.currentDirection = "up";
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentDirection += "-running";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.currentDirection = "down";
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentDirection += "-running";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.currentDirection = "left";
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentDirection += "-running";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.currentDirection = "right";
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentDirection += "-running";
            }
            //switches from moving to standing if key is not pressed, and not in attack animation
        } else if (!player.currentDirection.contains("attacking")){
            if("down".contains(player.currentDirection.toLowerCase()) ||
                    player.currentDirection.contains("down-running")){
                player.currentDirection = "down-standing";
            }else if("up".contains(player.currentDirection.toLowerCase()) ||
                    player.currentDirection.contains("up-running")){
                player.currentDirection = "up-standing";
            } else if("left".contains(player.currentDirection.toLowerCase()) ||
                    player.currentDirection.contains("left-running")){
                player.currentDirection = "left-standing";
            } else if("right".contains(player.currentDirection.toLowerCase()) ||
                    player.currentDirection.contains("right-running")){
                player.currentDirection = "right-standing";
            }
        }

        // logic to check if player is going out of bounds.
        Player temporaryPlayer = new Player(player);
        temporaryPlayer.calculateNextMove();

        // key frame does not matter, I believe...
        if (temporaryPlayer.sprite.getX() + player.sprite.getRegionWidth() >= fitViewport.getWorldWidth() ||
                temporaryPlayer.sprite.getX() < 0){
            System.out.println();

        } else if (temporaryPlayer.sprite.getY() + player.sprite.getRegionHeight() >= fitViewport.getWorldHeight() ||
                temporaryPlayer.sprite.getY() < 0 ) {
        }

        else {
            //check collision with any of the objects of the game.
            if(!level.collides(temporaryPlayer)){

                player.calculateNextMove(); // move the player along the current direction
            }
            else if (level.collides(temporaryPlayer)){

            }
        }
        // move player into required direction here.
    }


    //

    
}
