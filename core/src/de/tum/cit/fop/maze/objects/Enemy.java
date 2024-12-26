package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

import java.util.HashMap;
import java.util.Map;

public class Enemy extends Obstacle{

    static final int spriteSheetColumn = 9;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    public static final String spriteSheetFilePath = "mobs.png";

    // let the sprite be the first animation in the animationMap.
    // this means, we need to move from there to the right and/or downwards to get the remaining animations
    // everything in a row is in a direction
    public Map<String, Animation<Sprite>> animationMap;

    public Enemy(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight){
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        this.animationMap = new HashMap<>();
        LoaderHelper.loadEnemyDirectionAnimations(this);
    }

    public Enemy(float x, float y){
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

}
