package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.collectables.Key;
import de.tum.cit.fop.maze.objects.collectables.Life;
import de.tum.cit.fop.maze.objects.collectables.RandomKill;
import de.tum.cit.fop.maze.objects.enemy.Enemy;
import de.tum.cit.fop.maze.objects.obstacles.Trap;
import de.tum.cit.fop.maze.utilities.ScalingFactor;

/**
 * Superclass to wall, entry, exit, trap and enemy.
 */
public abstract class GameObject {

    public Sprite sprite;

    public GameObject(){}

    public GameObject(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight){
        // loading the texture into the sprite
        loadSprite(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public void draw(SpriteBatch spriteBatch, Player player) {
        this.sprite.draw(spriteBatch);
    };

    /**
     * Contains logic to load the texture from the sprite sheet file.
     */
    public void loadSprite(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String filePath, int objectWidth, int objectHeight) {
        Texture tileSheet = new Texture(Gdx.files.internal(filePath));

        TextureRegion textureRegion = new TextureRegion(tileSheet,
                spriteSheetColumn * objectWidth, spriteSheetRow * objectHeight, // gives out coordinates in spriteSheet
                objectWidth, objectHeight);
        this.sprite = new Sprite(textureRegion);
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
            System.out.println("Region Width: " + this.sprite.getRegionWidth() + " Height: " + this.sprite.getRegionHeight());
            System.out.println("Width: " + this.sprite.getWidth() + " Height: " + this.sprite.getHeight());
            System.out.println("Collision Width: " + this.sprite.getBoundingRectangle().getWidth() + " Height: " + this.sprite.getBoundingRectangle().getHeight());

            return true;
        }
        return false;
    }


    public static GameObject convertToGameObject(int objectType, float unscaledX, float unscaledY, ScalingFactor scalingFactor){
        GameObject object;
        float x = unscaledX * scalingFactor.width;
        float y = unscaledY * scalingFactor.height;
        if (objectType == 0){
            object = new Wall(x, y);
        } else if (objectType == 1) {
            object = new EntryPoint(x, y);
        } else if(objectType == 2) {
            object = new Exit(x,y);
        } else if (objectType == 3) {
            object = new Trap(x, y);
        } else if (objectType == 4) {
            object = new Enemy(x, y);
        } else if (objectType == 5){
            object = new Key(x, y);
        } else if (objectType == 6){
            object = new EntryPoint(x, y);
        } else if (objectType == 7){ // random kill super power
            object = new RandomKill(x, y);
        } else if (objectType == 8){ // heart
            object = new Life(x, y);
        }else {
            object =  new RandomKill(x, y);
        }

        return object;
    }

    public float calculateSurface(){
        Rectangle rectangle = this.sprite.getBoundingRectangle();

        return rectangle.area();
    }
}
