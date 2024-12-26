package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.math.Vector2;

public class Obstacle extends GameObject{

    public Obstacle(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow,
                    String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    @Override
    public boolean collide(Player player) {
        return super.collide(player);
    }
}
