package de.tum.cit.fop.maze.utilities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.MazeRunnerGame;
import de.tum.cit.fop.maze.objects.collectables.RandomKill;
import de.tum.cit.fop.maze.objects.enemy.Enemy;

import java.util.List;

/**
 * Class which handles the user input with respect to the player character.
 *
 * Logic is currently very fragile.
 */
public class LogicHandler {

    public static void input(MazeRunnerGame game, Player player,
                             FitViewport fitViewport, Level level, float delta) {

        // code to keep track of attack animation
        if(player.currentState.toLowerCase().contains("attacking")){
            player.attackFramesCounter++;
            if (player.attackFramesCounter > 10){
                player.currentState = player.currentState.replace("attacking","standing");
                player.attackFramesCounter = 0;
            }
        }

        // logic for initiating attack, lasts 10 (frames ?)
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            // remove randomly if superpower picked up
            if(player.superPower != null && player.superPower.getClass() == RandomKill.class){
                @SuppressWarnings("unchecked")
                List<Enemy> enemyList = (List<Enemy>) (Object) level.gameObjects.get(4);
                if( !enemyList.isEmpty()) {
                    int random = (int) (Math.random() * enemyList.size());
                    enemyList.remove(random);
                    player.superPower = null;
                }
            }
            if (player.currentState.toLowerCase().contains("down")){
                player.currentState = "down-attacking";
            } else if (player.currentState.toLowerCase().contains("up")){
                player.currentState = "up-attacking";
            } else if (player.currentState.toLowerCase().contains("left")){
                player.currentState = "left-attacking";
            } else if (player.currentState.toLowerCase().contains("right")){
                player.currentState = "right-attacking";
            }
            return;
        }

        // movement animation
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.currentState = "up";
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentState += "-running";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.currentState = "down";
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentState += "-running";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.currentState = "left";
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentState += "-running";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.currentState = "right";
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
                player.currentState += "-running";
            }
            //switches from moving to standing if key is not pressed, and not in attack animation
        } else if (!player.currentState.contains("attacking")){
            if("down".contains(player.currentState.toLowerCase()) ||
                    player.currentState.contains("down-running")){
                player.currentState = "down-standing";
            }else if("up".contains(player.currentState.toLowerCase()) ||
                    player.currentState.contains("up-running")){
                player.currentState = "up-standing";
            } else if("left".contains(player.currentState.toLowerCase()) ||
                    player.currentState.contains("left-running")){
                player.currentState = "left-standing";
            } else if("right".contains(player.currentState.toLowerCase()) ||
                    player.currentState.contains("right-running")){
                player.currentState = "right-standing";
            }
        }


        Player temporaryPlayer = new Player(player);
        temporaryPlayer.calculateNextMove(false);

        if(!level.collides(temporaryPlayer)){
            player.calculateNextMove(true); // move the player along the current direction//
        }

        // move player into required direction here.
    }


    //

    
}
