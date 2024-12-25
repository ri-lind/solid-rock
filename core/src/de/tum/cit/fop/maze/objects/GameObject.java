package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

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

    // are coordinates origin or bottom left corner? I guess it is up to me

    public void draw(){
    }

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

    public static GameObject convertToGameObject(int objectType, float x, float y, int objectScale){
        GameObject gameObject;
        if (objectType == 0){
            gameObject = new Wall(x, y);
        } else if (objectType == 1) {
            gameObject = new EntryPoint(x, y);
        } else if(objectType == 2) {
            gameObject = new Exit(x,y);
        } else{
            // not defined gameobject, we replace the textures with the exit texture.
            gameObject = new Exit(x, y);
        }
        // scale the
        //gameObject.sprite.setSize(gameObject.sprite.getRegionWidth() * objectScale, gameObject.sprite.getRegionHeight() * objectScale);
        System.out.println(gameObject.sprite.getBoundingRectangle().getX());
        gameObject.sprite.setScale(gameObject.sprite.getScaleX() * objectScale);
        return gameObject;
    }

}
