package de.tum.cit.fop.maze.objects.hud;

import com.badlogic.gdx.math.Vector2;

public class RightDirection {

    Vector2 playerCenter;
    Vector2 exitCenter;


    int isExitAbove;

    public RightDirection(Vector2 playerCenter, Vector2 exitCenter){
        this.playerCenter = playerCenter;
        this.exitCenter = exitCenter;

        if (exitCenter.y > playerCenter.y){
            this.isExitAbove = 1;
        } else {
            this.isExitAbove = 0;
        }
    }


    public Vector2 rightDirectionVector(){
        if (exitCenter.y > playerCenter.y){
            return  exitCenter.sub(playerCenter);
        }
        return playerCenter.sub(exitCenter);
    }
}
