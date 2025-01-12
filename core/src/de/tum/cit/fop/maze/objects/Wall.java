package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.obstacles.Obstacle;

public class Wall extends Obstacle {


    static final int spriteSheetColumn = 5;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    static final String spriteSheetFilePath = "basictiles.png";

    public Wall(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, int objectWidth, int objectHeight, String spriteSheetFilePath){
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public Wall(float x, float y){
        this(new Vector2(x, y), Wall.spriteSheetColumn, Wall.spriteSheetRow, Wall.objectWidth, Wall.objectHeight, spriteSheetFilePath);
    }

}