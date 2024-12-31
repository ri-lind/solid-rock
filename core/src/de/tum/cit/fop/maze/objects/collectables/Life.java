package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.objects.hud.Heart;

public class Life extends SuperPower{


    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 512;
    static final int objectHeight = 512;
    static final String spriteSheetFilePath = "heart.png";


    public Life(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public Life(float x, float y) {
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    // not the true player!
    public boolean collide(Player player){
        // the numbers chose for
        Heart heart = player.heart;
        if (this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            System.out.println(heart.remaining_life);
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
