package de.tum.cit.fop.maze.objects.collectables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Player;

// give the number
public class RandomKill extends SuperPower{


    static Sound randomKillPickUp = Gdx.audio.newSound(Gdx.files.internal("sounds/random_kill_pickup.mp3"));
    public static Sound randomKillAttackSound = Gdx.audio.newSound(Gdx.files.internal("sounds/random_kill_attack.mp3"));
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

    @Override
    public boolean collide(Player player){
        // the numbers chose for
        if (this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            this.shouldBeRemoved = true;
            randomKillPickUp.play(0.1f);
            System.out.print(this.getClass() + ", surface: " + this.calculateSurface() + " units square. ");
            System.out.println("Region Width: " + this.sprite.getRegionWidth() + " Height: " + this.sprite.getRegionHeight());
            System.out.println("Width: " + this.sprite.getWidth() + " Height: " + this.sprite.getHeight());
            System.out.println("Collision Width: " + this.sprite.getBoundingRectangle().getWidth() + " Height: " + this.sprite.getBoundingRectangle().getHeight());

            return true;
        }
        return false;
    }

}
