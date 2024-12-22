package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class Player {
    // down, right, up, left.
    public Dictionary<String, Animation<TextureRegion>> directionAnimations;

    // should be only left, right, down, up or empty string
    public String currentDirection = "";
    // position
    public int x;
    public int y;

    private final float SPEED = 1f;

    // used for calculating world boundaries of player
    public int width;
    public int height;

    public Player(){
        x = 0;
        y = 0;
        this.directionAnimations = new Hashtable<String, Animation<TextureRegion>>();
    }
    public Player(int x, int y, String currentDirection){
        this.x = x;
        this.y = y;
        this.currentDirection = currentDirection;
    }


    // for usage in the input dimension
    public void calculateNextMove(){
        if ("down".contains(currentDirection.toLowerCase())){ // down
            y-=SPEED;
        } else if("right".contains(currentDirection.toLowerCase())) { // right
            x+=SPEED;
        } else if ("up".contains(currentDirection.toLowerCase())) {// up
            y+=SPEED;
        } else if ("left".contains(currentDirection.toLowerCase())) {// left
            x-=SPEED;
        }
        else return; // no update, if the key pressed is not good.
    }

    public Animation<TextureRegion> getCurrentAnimation(){
        return this.directionAnimations.get(currentDirection.toLowerCase());
    }
}