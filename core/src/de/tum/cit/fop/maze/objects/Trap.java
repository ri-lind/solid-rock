package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

/**
 * Actually a bomb.
 */
public class Trap extends Obstacle{

    public Animation<Sprite> animations;

    // used for sprite
    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 32;
    static final int objectHeight = 64;
    static final String spriteSheetFilePath = "BombExploding.png";

    public boolean triggered;
    private float stateTime;

    public Trap(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);

        LoaderHelper.loadTrapAnimations(this, objectWidth, objectHeight, spriteSheetFilePath, coordinates.x, coordinates.y);
        this.sprite = this.animations.getKeyFrames()[0]; // override of loadsprite of gameobject
        this.triggered = false;
        this.stateTime = 0;
    }

    public Trap(float x, float y){
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    @Override
    public boolean collide(Player player){

        if (this.sprite.getBoundingRectangle().overlaps(player.sprite.getBoundingRectangle())){
            this.triggered = true;
            System.out.print(this.getClass() + ", surface: " + this.calculateSurface() + " units square. ");
            System.out.println("Width: " + this.sprite.getRegionWidth() + " Height: " + this.sprite.getRegionHeight());
            System.out.println("Width: " + this.sprite.getWidth() + " Height: " + this.sprite.getHeight());

            return true;
        }
        return false;
    }

    /**
     * Logic of this specific method should be:
     * triggered trap - animate explosion on screen
     * else - animate normal sprite
     */
    @Override
    public void draw(SpriteBatch spriteBatch) {
        Sprite sprite;
        if(triggered){
            System.out.println("We here");
            this.stateTime += Gdx.graphics.getDeltaTime();
            sprite = this.animations.getKeyFrame(stateTime, false);
            sprite.setPosition(this.sprite.getX(), this.sprite.getY());
        }else{
            sprite = this.sprite;
        }
        sprite.draw(spriteBatch);
    }


}
