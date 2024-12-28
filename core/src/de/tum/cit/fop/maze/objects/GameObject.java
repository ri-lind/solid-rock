package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;

/**
 * Superclass to wall, entry, exit, trap and enemy.
 */
public abstract class GameObject {

    public Sprite sprite;
    /*
    coordinates they take from the file
    textures they take from the pngs

     */

    public GameObject(){}

    public GameObject(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight){
        // loading the texture into the sprite
        loadSprite(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public abstract void draw(float stateTime);

    /**
     * Contains logic to load the texture from the sprite sheet file.
     */
    public void loadSprite(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String filePath, int objectWidth, int objectHeight) {
        Texture tileSheet = new Texture(Gdx.files.internal(filePath));

        // fetch the wall texture

        // the 6th tile in the first row
        TextureRegion textureRegion = new TextureRegion(tileSheet,
                spriteSheetColumn * objectWidth, spriteSheetRow * objectHeight, // gives out coordinates in spriteSheet
                objectWidth, objectHeight);

        // instead of setting x and y, adjust sprite position.
        this.sprite = new Sprite(textureRegion);
        // update the origin MANUALLY, this is such bullshit
        this.sprite.setOrigin(this.sprite.getOriginX() + coordinates.x, this.sprite.getOriginY() + coordinates.y);
        sprite.setBounds(coordinates.x, coordinates.y, objectWidth, objectHeight);
    }




    /**
     * returns true if there is a collision with the player.
     * @param player
     * @return
     */
    public boolean collide(Player player){
        // the numbers chose for
        if (this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            System.out.print(this.getClass() + ", surface: " + this.calculateSurface() + " units square. ");
            System.out.println("Width: " + this.sprite.getRegionWidth() + " Height: " + this.sprite.getRegionHeight());
            System.out.println("Width: " + this.sprite.getWidth() + " Height: " + this.sprite.getHeight());

            return true;
        }
        return false;
    }


    public static GameObject convertToGameObject(int objectType, float x, float y){
        GameObject obstacle;
        if (objectType == 0){
            obstacle = new Wall(x, y);
        } else if (objectType == 1) {
            obstacle = new EntryPoint(x, y);
        } else if(objectType == 2) {
            obstacle = new Exit(x,y);
        } else if (objectType == 3) {
            obstacle = new Trap(x, y);
        } else if (objectType == 4) {
            obstacle = new Enemy(x, y);
        }
        else{
            // default to enemy spawning
            obstacle = new Enemy(x, y);
        }

        //obstacle.sprite.setScale(objectScale); // add or remove the scaling.
        return obstacle;
    }

    public float calculateSurface(){
        Rectangle rectangle = this.sprite.getBoundingRectangle();

        return rectangle.area();
    }
}
