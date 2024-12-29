package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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
    static final String spriteSheetFilePath = "bomb_blue.png";

    public boolean triggered;
    private float stateTime;
    private boolean damageDealt;

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
    public void draw(SpriteBatch spriteBatch, Player player) {
        Sprite sprite;
        if(triggered){
            this.stateTime += Gdx.graphics.getDeltaTime();
            sprite = this.animations.getKeyFrame(stateTime, false);
            sprite.setPosition(this.sprite.getX(), this.sprite.getY());
            // to check when to deal damage.
            if(!this.animations.isAnimationFinished(stateTime)){
                int frameIndex = this.animations.getKeyFrameIndex(stateTime);
                if (frameIndex >=7 && frameIndex <= 9 && !damageDealt) {
                    explode(player);
                    damageDealt = true;
                }
            }
        }else{
            sprite = this.sprite;
        }
        sprite.draw(spriteBatch);
    }

    // this method is called at the height of the explosion. It should damage the player if the player is at a range of
    // damage range is triple the size of the normal sprite but around it. So center it at the sprite center.
    public void explode(Player player){
        // set up explosion range.
        Rectangle explosionDamageRange = new Rectangle(this.sprite.getBoundingRectangle());
        explosionDamageRange.setSize(explosionDamageRange.width*3, explosionDamageRange.height*3);
        if(player.sprite.getBoundingRectangle().overlaps(explosionDamageRange)){
            player.heart.sustainsDamage();
        }
    }


}
