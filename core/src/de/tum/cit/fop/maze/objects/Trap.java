package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;

public class Trap extends Obstacle{

    static final int spriteSheetColumn = 5;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    static final String spriteSheetFilePath = "basictiles.png";

    public Trap(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public Trap(float x, float y){
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }
}
