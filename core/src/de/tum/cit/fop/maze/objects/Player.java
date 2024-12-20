package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Objects;

public class Player {
    // down, right, up, left.
    public Array<Animation<TextureRegion>> directionAnimations = new Array<>(4);

    // these down here are to be deleted
    private int indexOfCurrentDirection;

    public Sprite sprite;
    public String currentDirection = "";

    public int x;
    public int y;

    public Player(){
        sprite = new Sprite();
        x = 0;
        y = 0;
    }

    private int getIndexOfCurrentDirection() {

        if (Objects.equals(currentDirection, "DOWN"))
            return 0;
        if (Objects.equals(currentDirection, "RIGHT"))
            return 1;
        if (Objects.equals(currentDirection, "UP"))
            return 2;
        if (Objects.equals(currentDirection, "LEFT"))
            return 3;

        return indexOfCurrentDirection;
    }

    public Animation<TextureRegion> getCurrentAnimation(){
        return directionAnimations.get(getIndexOfCurrentDirection());
    }
}