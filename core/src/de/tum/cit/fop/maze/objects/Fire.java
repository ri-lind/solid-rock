package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Fire extends GameObject{

    static final int spriteSheetColumn = 4;
    static final int spriteSheetRow = 3;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    static final String spriteSheetFilePath = "objects.png";

    public boolean shouldBeRemoved = false;

    public Fire(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow,
               String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        this.sprite.setSize(16, 16);
    }


    public boolean collide(Player player){
        // the numbers chose for
        if (this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            System.out.print(this.getClass() + ", surface: " + this.calculateSurface() + " units square. ");
            System.out.println("Region Width: " + this.sprite.getRegionWidth() + " Height: " + this.sprite.getRegionHeight());
            System.out.println("Width: " + this.sprite.getWidth() + " Height: " + this.sprite.getHeight());
            System.out.println("Collision Width: " + this.sprite.getBoundingRectangle().getWidth() + " Height: " + this.sprite.getBoundingRectangle().getHeight());

            return false;
        }
        return false;
    }

    public Fire(float x, float y) {
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }


    @Override
    public void draw(SpriteBatch spriteBatch, Player player) {
        if(this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            player.heart.sustainsDamage(); // deal damage
            this.shouldBeRemoved = true;
        } else {
            this.sprite.draw(spriteBatch);
        }
    }

}
