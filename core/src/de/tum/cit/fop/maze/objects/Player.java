package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Player {
    // down, right, up, left.
    public Array<Animation<TextureRegion>> directionAnimations = new Array<>(4);
    // should be only left, right, down, up or empty string
    public String currentDirection = "";
    // position
    public int x;
    public int y;

    private final float SPEED = 1f;

    public Player(){
        x = 0;
        y = 0;
    }
    public Player(int x, int y, String currentDirection){
        this.x = x;
        this.y = y;
        this.currentDirection = currentDirection;
    }


    public Animation<TextureRegion> getCurrentAnimation(){
        return directionAnimations.get(getIndexOfCurrentDirection());
    }

    // for usage in the input dimension
    public void calculateNextMove(){
        if (currentDirection.toLowerCase().contains("down")){ // down
            y-= SPEED;
        } else if(currentDirection.toLowerCase().contains("right")) { // right
            x+=SPEED;
        } else if ( currentDirection.toLowerCase().contains("up")) {// up
            y+=SPEED;
        } else if (currentDirection.toLowerCase().contains("left")) {// left
            x-=SPEED;
        } else return; // no update, if the key pressed is not good.
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
        else return 1; // let's see if we can return right
    }

}