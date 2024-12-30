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

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Enemy extends Obstacle {

    static final int spriteSheetColumn = 9;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    public static final String spriteSheetFilePath = "mobs_green.png";

    public Circle hitBox;
    public Circle attackBox; // the enemy attacks
    public boolean toBeRemoved;

    public float timeSinceAttackedThePlayer = 0; // to keep track of damage we have dealt
    public boolean damageDealt = false;
    public boolean followingPlayer = false;
    public Circle followBox;
    public Instant movementStarted;


    public Breadcrumb goal;


    // let the sprite be the first animation in the animationMap.
    // this means, we need to move from there to the right and/or downwards to get the remaining animations
    // everything in a row is in a direction
    public Map<String, Animation<Sprite>> animationMap;

    public Enemy(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        this.animationMap = new HashMap<>();
        LoaderHelper.loadEnemyDirectionAnimations(this);

        hitBox = new Circle();
        hitBox.setPosition(coordinates.x + objectWidth, coordinates.y + objectHeight);
        hitBox.setRadius(25f);

        attackBox = new Circle();
        attackBox.setPosition(coordinates.x + objectWidth, coordinates.y + objectHeight);
        attackBox.setRadius(20f);

        toBeRemoved = false;

        followBox = new Circle();
        followBox.setPosition(coordinates.x + objectWidth, coordinates.y + objectHeight);
        followBox.setRadius(30f);

    }

    public Enemy(float x, float y) {
        this(new Vector2(x, y), spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
    }

    /**
     * Method for receiving player attacks.
     *
     * @param spriteBatch
     * @param player
     */
    @Override
    public void draw(SpriteBatch spriteBatch, Player player) {
        if(this.overlapsFollowBox(player) && !followingPlayer){
            new Breadcrumb(player);
            this.followingPlayer = true;
        } if(this.followingPlayer && movementStarted == null){
            followPlayerBreadcrumb();
        }else if(movementStarted!= null && Duration.between(movementStarted, Instant.now()).toSeconds() > 2 ){
            followPlayerBreadcrumb();
        }
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        Vector2 enemyCenter = new Vector2();
        this.sprite.getBoundingRectangle().getCenter(enemyCenter);

        if (this.overlapsHitbox(player)) {
            playerAttack(player, playerCenter, enemyCenter);
        }
        if(this.overlapsAttackBox(player) && !damageDealt){
            player.heart.sustainsDamage();
            this.damageDealt = true;
            this.timeSinceAttackedThePlayer += System.currentTimeMillis();
        }

        if(this.damageDealt){
            if((System.currentTimeMillis()-this.timeSinceAttackedThePlayer) > 200){
                this.timeSinceAttackedThePlayer = 0;
                this.damageDealt = false;
            }
        }

        this.sprite.draw(spriteBatch);
    }

    /**
     * Both receiving logic and giving out as well.
     * @param player
     * @param playerCenter
     * @param enemyCenter
     */
    public void playerAttack(Player player, Vector2 playerCenter, Vector2 enemyCenter) {
        if (player.currentState.contains("attacking")) {
            if (player.currentState.contains("left") && playerCenter.x > enemyCenter.x) {
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
            if (player.currentState.contains("right") && playerCenter.x < enemyCenter.x) {
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
            if (player.currentState.contains("up") && playerCenter.y < enemyCenter.y) {
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }
            if (player.currentState.contains("down") && playerCenter.y > enemyCenter.y) {
                System.out.println("Attack succeeded!");
                this.toBeRemoved = true;
            }

        }
    }

    private boolean overlapsHitbox(Player player) {
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        return this.hitBox.contains(playerCenter);
    }

    private boolean overlapsAttackBox(Player player){
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        return this.attackBox.contains(playerCenter);
    }
    private boolean overlapsFollowBox(Player player) {
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        return this.followBox.contains(playerCenter);
    }

    private void followPlayerBreadcrumb(){
        Optional<Breadcrumb> toFollow = Breadcrumb.calculateFarthestInRange(this);
        toFollow.ifPresent(breadcrumb -> this.goal = breadcrumb);
        moveToBreadCrumb();
        this.movementStarted = Instant.now();
    }

    private void moveToBreadCrumb(){

        if(this.goal == null)
            return;

        float averageOfX = (this.sprite.getX() + this.goal.sprite.getX())/2;
        float averageOfY = (this.sprite.getY() + this.goal.sprite.getY())/2;

        float xMovement = averageOfX - this.sprite.getX();
        float yMovement = averageOfY - this.sprite.getY();

        this.sprite.translateX(xMovement);
        this.sprite.translateY(yMovement);
    }

}
