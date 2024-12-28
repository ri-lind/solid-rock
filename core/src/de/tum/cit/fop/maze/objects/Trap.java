package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

/**
 * Actually a bomb.
 */
public class Trap extends Obstacle{

    public Animation<Sprite> animations;

    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 32;
    static final int objectHeight = 64;
    static final String spriteSheetFilePath = "BombExploding.png";

    public boolean playerNearby = false;

    public Trap(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        LoaderHelper.loadTrapAnimations(this, spriteSheetColumn, spriteSheetRow, objectWidth, objectHeight, spriteSheetFilePath);
        this.playerNearby = false;
    }

    public Trap(float x, float y){
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }
}
