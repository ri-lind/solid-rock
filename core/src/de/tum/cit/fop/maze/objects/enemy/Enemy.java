package de.tum.cit.fop.maze.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Obstacle;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.utilities.LoaderHelper;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends Obstacle {

    static final int spriteSheetColumn = 9;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    public static final String spriteSheetFilePath = "mobs_green.png";

    public Circle hitBox;
    public boolean toBeRemoved;
    public float stateTime = 0;



    // let the sprite be the first animation in the animationMap.
    // this means, we need to move from there to the right and/or downwards to get the remaining animations
    // everything in a row is in a direction
    public Map<String, Animation<Sprite>> animationMap;

    public Enemy(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight){
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        this.animationMap = new HashMap<>();
        LoaderHelper.loadEnemyDirectionAnimations(this);

        hitBox = new Circle();
        hitBox.setPosition(coordinates.x + objectWidth, coordinates.y + objectHeight);
        hitBox.setRadius(25f);
        toBeRemoved = false;
    }

    public Enemy(float x, float y){
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    /**
     * Make this method call inner logic for checking if player is close, if player has attacked in
     * the enemy's direction, and more.
     * @param spriteBatch
     * @param player
     */
    @Override
    public void draw(SpriteBatch spriteBatch, Player player){
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        Vector2 enemyCenter = new Vector2();
        this.sprite.getBoundingRectangle().getCenter(enemyCenter);

        if (this.overlaps(player)){
            stateTime += Gdx.graphics.getDeltaTime();
            checkForPlayerAttack(player, playerCenter, enemyCenter);
            damagePlayer(player, playerCenter, enemyCenter);
        }

        this.sprite.draw(spriteBatch);
    }

    public void damagePlayer(Player player, Vector2 playerCenter, Vector2 enemyCenter){

    }
    public void checkForPlayerAttack(Player player, Vector2 playerCenter, Vector2 enemyCenter){
        if(player.currentState.contains("attacking")){
            if(player.currentState.contains("left") && playerCenter.x > enemyCenter.x) {
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
            if (player.currentState.contains("right") && playerCenter.x < enemyCenter.x){
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
            if (player.currentState.contains("up") && playerCenter.y < enemyCenter.y){
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
            if(player.currentState.contains("down") && playerCenter.y > enemyCenter.y){
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
        }
    }

    private boolean overlaps(Player player){
        Circle circle = this.hitBox;

        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);

        return circle.contains(playerCenter);
    }

}
