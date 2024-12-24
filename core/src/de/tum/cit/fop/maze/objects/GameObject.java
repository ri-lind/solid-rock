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
        loadTexture(spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        // setting the coordinates
        this.sprite.setX(coordinates.x);
        this.sprite.setY(coordinates.y);
    }

    // are coordinates origin or bottom left corner? I guess it is up to me

    public void draw(){
    }

    /**
     * Contains logic to load the texture from the sprite sheet file.
     */
    public void loadTexture(int spriteSheetColumn, int spriteSheetRow, String filePath, int objectWidth, int objectHeight) {
        Texture tileSheet = new Texture(Gdx.files.internal(filePath));

        // fetch the wall texture

        // the 6th tile in the first row
        TextureRegion textureRegion = new TextureRegion(tileSheet,
                spriteSheetColumn * objectWidth, spriteSheetRow * objectHeight, // gives out coordinates in spriteSheet
                objectWidth, objectHeight);
        this.sprite = new Sprite(textureRegion);
    }



}
