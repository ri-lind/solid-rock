package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Player;

public abstract class SuperPower extends GameObject {


    public boolean shouldBeRemoved;

    public SuperPower(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow,
               String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        this.sprite.setSize(16, 16);
        this.shouldBeRemoved = false;
    }



    public boolean collide(Player player){
        // the numbers chose for
        if (this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            this.shouldBeRemoved = true;
            System.out.print(this.getClass() + ", surface: " + this.calculateSurface() + " units square. ");
            System.out.println("Region Width: " + this.sprite.getRegionWidth() + " Height: " + this.sprite.getRegionHeight());
            System.out.println("Width: " + this.sprite.getWidth() + " Height: " + this.sprite.getHeight());
            System.out.println("Collision Width: " + this.sprite.getBoundingRectangle().getWidth() + " Height: " + this.sprite.getBoundingRectangle().getHeight());

            return true;
        }
        return false;
    }
}
