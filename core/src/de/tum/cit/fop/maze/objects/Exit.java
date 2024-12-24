package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;

public class Exit extends GameObject {


    static final int spriteSheetColumn = 2;
    static final int spriteSheetRow = 6;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    static final String spriteSheetFilePath = "basictiles.png";


    public Exit(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, int objectWidth, int objectHeight, String spriteSheetFilePath){
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public Exit(float x, float y){
        this(new Vector2(x, y), Exit.spriteSheetColumn, Exit.spriteSheetRow, Exit.objectWidth, Exit.objectHeight, spriteSheetFilePath);
    }
}
