package de.tum.cit.fop.maze.objects.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.obstacles.Obstacle;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Enemy extends Obstacle {


    public float SPEED = 5f;

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

    public Vector2 center;

    // let the sprite be the first animation in the animationMap.
    // this means, we need to move from there to the right and/or downwards to get the remaining animations
    // everything in a row is in a direction
    public Map<String, Animation<Sprite>> animationMap;

    public Enemy(Vector2 coordinates, int spriteSheetColumn, int spriteSheetRow, String spriteSheetFilePath, int objectWidth, int objectHeight) {
        super(coordinates, spriteSheetColumn, spriteSheetRow, spriteSheetFilePath, objectWidth, objectHeight);
        this.animationMap = new HashMap<>();
        LoaderHelper.loadEnemyDirectionAnimations(this);

        Vector2 enemyCenter = new Vector2();
        this.sprite.getBoundingRectangle().getCenter(enemyCenter);
        this.center = enemyCenter;

        hitBox = new Circle();
        hitBox.setPosition(this.center);
        hitBox.setRadius(30f);

        attackBox = new Circle();
        attackBox.setPosition(this.center);
        attackBox.setRadius(10f);

        toBeRemoved = false;

        followBox = new Circle();
        followBox.setPosition(this.center);
        followBox.setRadius(60f);

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


        //check if the breadcrumb was followed for 200 milliseconds
        if(this.followingPlayer && Duration.between(this.movementStarted, Instant.now()).toMillis() > 200){
            this.followingPlayer = false;
            this.movementStarted = Instant.now();

        }
        if(!followingPlayer){
            for (int i = 0; i < Breadcrumb.allPlayerBreadCrumbs.size(); i++){
                Breadcrumb breadcrumb = Breadcrumb.allPlayerBreadCrumbs.get(i);
                Vector2 breadCrumbCenter = new Vector2();
                breadcrumb.sprite.getBoundingRectangle().getCenter(breadCrumbCenter);
                if(this.followBox.contains(breadCrumbCenter) && !this.followingPlayer){
                    this.movementStarted = Instant.now();
                    this.followingPlayer = true;
                    followPlayerBreadcrumb(breadcrumb);
                    break;
                }
            }
        }
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        Vector2 enemyCenter = new Vector2();
        this.sprite.getBoundingRectangle().getCenter(enemyCenter);
        // main logic for proximity action
        damageDealingLogic(player, playerCenter, enemyCenter);


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
        this.hitBox.setPosition(new Vector2(this.sprite.getX(), this.sprite.getY())); // update the hitbox
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

    private void damageDealingLogic(Player player, Vector2 playerCenter, Vector2 enemyCenter){
        if (this.overlapsHitbox(player)) {
            playerAttack(player, playerCenter, enemyCenter);
        }
        if(this.overlapsAttackBox(player) && !damageDealt){
            player.heart.sustainsDamage();
            this.damageDealt = true;
            this.timeSinceAttackedThePlayer += System.currentTimeMillis();
        }

        // does not work correctly
        if(this.damageDealt){
            if((System.currentTimeMillis()-this.timeSinceAttackedThePlayer) > 200){
                this.timeSinceAttackedThePlayer = 0;
                this.damageDealt = false;
            }
        }
    }
    // implement this logic later. For now, just make the enemy chase the player when in range.
    private void followPlayerBreadcrumb(Breadcrumb breadCrumb){
        // we have speed. this method is called 60 times per second more or less.
        Vector2 breadcrumbCenter = new Vector2();
        breadCrumb.sprite.getBoundingRectangle().getCenter(breadcrumbCenter);

        if(breadCrumb.sprite.getX() < this.sprite.getX()){
            this.sprite.translateX(-this.SPEED);
            this.center.add(-this.SPEED, 0);
        }
        if(breadCrumb.sprite.getX() > this.sprite.getX()){
            this.sprite.translateX(this.SPEED);
            this.center.add(this.SPEED, 0);
        }
        if(breadCrumb.sprite.getY() < this.sprite.getY()){
            this.sprite.translateY(-this.SPEED);
            this.center.add(0, -this.SPEED);
        }
        if(breadCrumb.sprite.getY() > this.sprite.getY()){
            this.sprite.translateY(this.SPEED);
            this.center.add(0, this.SPEED);
        }

        this.attackBox.setPosition(this.center);
        this.hitBox.setPosition(this.center);
        this.followBox.setPosition(this.center);
    }

}
