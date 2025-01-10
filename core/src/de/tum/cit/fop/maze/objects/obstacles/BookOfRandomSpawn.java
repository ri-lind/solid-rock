package de.tum.cit.fop.maze.objects.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

public class BookOfRandomSpawn extends Obstacle{


    static final int spriteSheetColumn = 0;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    static final String spriteSheetFilePath = "objects.png";

    public Animation<Sprite> animations;

    public boolean triggered = false;
    private float stateTime;
    public boolean shouldBeRemoved = false;

    public boolean spawnFire;
    public BookOfRandomSpawn(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);

        LoaderHelper.loadBookOfRandomSpawnAnimations(this, objectWidth, objectHeight, spriteSheetFilePath, coordinates.x, coordinates.y);

        // add logic to lood animation sprite of this animations

         // override of loadsprite of gameobject

        this.sprite = this.animations.getKeyFrames()[0];
    }

    public BookOfRandomSpawn(float x, float y){
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

    @Override
    public void draw(SpriteBatch spriteBatch, Player player){
        Sprite sprite;
        if(triggered){
            this.stateTime += Gdx.graphics.getDeltaTime();
            sprite = this.animations.getKeyFrame(stateTime, false);
            if(this.animations.isAnimationFinished(stateTime)){
                this.shouldBeRemoved = true;
                randomSpawn();
            }
        }else {
            sprite = this.sprite;
        }

        sprite.draw(spriteBatch);
    }

    public void randomSpawn(){
        float randomNumber = (float) Math.random();

        this.spawnFire = randomNumber <= 0.5f;
    }

    public void spawnFire(Level level){
        // spawn a fire and add it to the objects of the level. Not in maze file originally, dynamic in runtime.

    }
    public void spawnLife(Level level){}
}
