package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;

public class EntryPoint extends GameObject{

    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 6;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    static final String spriteSheetFilePath = "basictiles.png";


    public EntryPoint(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, int objectWidth, int objectHeight, String spriteSheetFilePath){
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public EntryPoint(float x, float y){
        this(new Vector2(x, y), EntryPoint.spriteSheetColumn, EntryPoint.spriteSheetRow, EntryPoint.objectWidth, EntryPoint.objectHeight, spriteSheetFilePath);
    }
}
