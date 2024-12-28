package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Needs wayyyy more work.
 */
public class Key extends GameObject implements Collectable{

    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 0;
    static final int objectHeight = 0;
    static final String spriteSheetFilePath = "";

    public Key(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow,
                    String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }


    public Key(float x, float y) {
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    public Key()
    {
        this.sprite = new Sprite(new Texture(Gdx.files.internal("")));
    };
}
