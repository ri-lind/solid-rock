package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Dictionary;
import java.util.Hashtable;


public class Player {
    // down, right, up, left.
    public Dictionary<String, Animation<TextureRegion>> animations;

    // the animations dictionary and calculate next move methods depend on this field
    public String currentDirection = "";
    // position
    public float x;
    public float y;

    private float SPEED = 1f;

    // used for calculating world boundaries of player
    public int width;
    public int height;

    public int attackFramesCounter = 0;

    public Player(){
        x = 0;
        y = 0;
        this.animations = new Hashtable<String, Animation<TextureRegion>>();
    }
    public Player(float x, float y, String currentDirection){
        this.x = x;
        this.y = y;
        this.currentDirection = currentDirection;
    }


    // for usage in the input dimension
    public void calculateNextMove(){
        if (currentDirection.toLowerCase().contains("running")){
            SPEED = 2f; // running is double the speed
        }
        if ("down".contains(currentDirection.toLowerCase()) ||
                currentDirection.toLowerCase().contains("down-running")){ // down
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
         SPEED = 1f; // reset speed after movement
    }

    public Animation<TextureRegion> getCurrentAnimation(){
        return this.animations.get(currentDirection.toLowerCase().replace("-running", ""));
    }
}