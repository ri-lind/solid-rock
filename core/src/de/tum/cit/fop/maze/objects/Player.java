package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents a player, in the sense that it contains the animations, coordinates and has some manipulation
 * logic in it. The focal center is the currentDirection attribute, which is heavily manipulated by this
 * class and the method InputHandler.input().
 */
public class Player {
    // down, right, up, left.
    public Dictionary<String, Animation<TextureRegion>> animations;

    // the animations dictionary and calculate next move methods depend on this field
    public String currentDirection = "";
    // position
    public float x;
    public float y;
    public Sprite sprite;

    private float SPEED = 1f;

    // used for calculating world boundaries of player
    public int width;
    public int height;

    public int attackFramesCounter = 0;

    // we load the sprite in the loaderhelper
    public Player(){
        x = 112;
        y = 300;
        this.animations = new Hashtable<>();
    }

    /**
     * Constructor used for boundary checking in the logic handler class.
     * @param x
     * @param y
     * @param currentDirection
     */
    public Player(float x, float y, String currentDirection){
        this.x = x;
        this.y = y;
        this.animations = new Hashtable<>();
        this.currentDirection = currentDirection;
    }


    // think about the objects the player cannot overlap with
    public void calculateNextMove(){

        if (currentDirection.toLowerCase().contains("running")){
            SPEED = 2f; // running is double the speed
        }
        if ("down".contains(currentDirection.toLowerCase()) || currentDirection.toLowerCase().contains("down-running")){
            y-=SPEED;
        } else if("right".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("right-running")) { // right
            x+=SPEED;
        } else if ("up".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("up-running")) {// up
            y+=SPEED;
        } else if ("left".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("left-running")) {// left
            x-=SPEED;
        }
        //update sprite coordinates
        sprite.setX(x);
        sprite.setY(y);
         SPEED = 1f; // reset speed after movement
    }

    public Animation<TextureRegion> getCurrentAnimation(){
        return this.animations.get(currentDirection.toLowerCase().replace("-running", ""));
    }
}