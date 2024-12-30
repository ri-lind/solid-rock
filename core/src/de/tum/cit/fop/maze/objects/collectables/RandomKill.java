package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Player;

// give the number
public class RandomKill extends SuperPower{

    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 512;
    static final int objectHeight = 512;
    static final String spriteSheetFilePath = "random_kill.png";



    public RandomKill(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public RandomKill(float x, float y) {
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

}
