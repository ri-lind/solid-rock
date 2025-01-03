package de.tum.cit.fop.maze.objects.obstacles;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.GameObject;

public abstract class Obstacle extends GameObject {


    public Obstacle(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow,
                    String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);

    }


}
