package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Player {
    // down, right, up, left.
    public Array<Animation<TextureRegion>> directionAnimations = new Array<>(4);

    // the index of the animation in the array above. Where the player is currently facing.
    private int indexOfCurrentDirection;

    // can be only left, right, down, up or empty string
    public String currentDirection = "";

    public int x;
    public int y;


    public Player(){
        x = 0;
        y = 0;
    }
    public Player(int x, int y, int indexOfCurrentDirection){
        this.x = x;
        this.y = y;
    }


    public Animation<TextureRegion> getCurrentAnimation(){
        return directionAnimations.get(getIndexOfCurrentDirection());
    }

    public void calculateNextMove(){
        if (getIndexOfCurrentDirection() == 0){ // down
            y -=1;
        } else if(getIndexOfCurrentDirection() == 1) { // right
            x+=1;
        } else if ( getIndexOfCurrentDirection() == 2) {// up
            y+=1;
        } else if (getIndexOfCurrentDirection() == 3) {// left
            x-=1;
        } else return;
    }

    public int getIndexOfCurrentDirection(){
        if (currentDirection.contains("UP"))
                return 2;
        else if ( currentDirection.contains("DOWN"))
            return 0;
        else if (currentDirection.contains("RIGHT"))
            return 1;
        else if (currentDirection.contains("LEFT"))
            return 3;
        else return 0; // let's see if we can return right
    }

}