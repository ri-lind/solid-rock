package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {

    public Animation<TextureRegion> characterDownAnimation;
    public Animation<TextureRegion> characterUpAnimation;
    public Animation<TextureRegion> characterLeftAnimation;
    public Animation<TextureRegion> characterRightAnimation;

    public Sprite sprite;
    public String currentDirection;

    public int x;
    public int y;

    public Player(){
        sprite = new Sprite();
        x = 0;
        y = 0;
    }
}