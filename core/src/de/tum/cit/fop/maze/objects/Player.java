package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents a player, in the sense that it contains the animations, coordinates and has some manipulation
 * logic in it. The focal center is the currentDirection attribute, which is heavily manipulated by this
 * class and the method InputHandler.input().
 */
public class Player {
    // down, right, up, left.
    public Dictionary<String, Animation<Sprite>> animations;

    // the animations dictionary and calculate next move methods depend on this field
    public String currentDirection = "";

    public Sprite sprite;

    private float SPEED = 1f;

    public int attackFramesCounter = 0;

    public Player(){

        this.animations = new Hashtable<>();
        LoaderHelper.loadCharacterDirectionAnimation(this); // Load character movement and standing animation
        LoaderHelper.loadCharacterAttackAnimations(this); // load the attack animations of the character
        this.currentDirection = "RIGHT";
        this.sprite.setX(100);
        this.sprite.setY(50);
        this.sprite.setSize(32, 32);

    }

    /**
     *
     * @param otherPlayer
     */
    public Player(Player otherPlayer){
        this.animations = new Hashtable<>();
        this.currentDirection = otherPlayer.currentDirection;
        // not really needed
        this.SPEED = otherPlayer.SPEED;
        this.attackFramesCounter = otherPlayer.attackFramesCounter;
        // needed
        LoaderHelper.loadCharacterDirectionAnimation(this);
        this.sprite.setBounds(otherPlayer.sprite.getX(), otherPlayer.sprite.getY(), otherPlayer.sprite.getRegionWidth(), otherPlayer.sprite.getRegionHeight());
    }


    // think about the objects the player cannot overlap with
    public void calculateNextMove(){

        if (currentDirection.toLowerCase().contains("running")){
            SPEED = 2 * SPEED; // running is double the speed
        }
        if ("down".contains(currentDirection.toLowerCase()) || currentDirection.toLowerCase().contains("down-running")){
            this.sprite.translateY(-SPEED);
        } else if("right".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("right-running")) { // right
            this.sprite.translateX(SPEED);
        } else if ("up".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("up-running")) {// up
            this.sprite.translateY(SPEED);
        } else if ("left".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("left-running")) {// left
            this.sprite.translateX(-SPEED);
        }
         SPEED = 1f; // reset speed after movement
    }

    public Animation<Sprite> getCurrentAnimation(){
        return this.animations.get(currentDirection.toLowerCase().replace("-running", ""));
    }
}