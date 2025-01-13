package de.tum.cit.fop.maze.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.collectables.RandomKill;
import de.tum.cit.fop.maze.objects.obstacles.Obstacle;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Enemy extends Obstacle {


    public float SPEED = 1f;

    static final int spriteSheetColumn = 9;
    static final int spriteSheetRow = 0;
    static final int objectWidth = 16;
    static final int objectHeight = 16;
    public static final String spriteSheetFilePath = "transparent_background/mobs.png";

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


    float stateTime = 0;

    public List<GameObject> walls;

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
        if(stateTime != 0){
            stateTime += Gdx.graphics.getDeltaTime();
        }

        //check if the breadcrumb was followed for 200 milliseconds
        if(this.followingPlayer && Duration.between(this.movementStarted, Instant.now()).toMillis() > 40){
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
                    stateTime += Gdx.graphics.getDeltaTime();

                    followPlayerBreadcrumb(breadcrumb);
                    break;
                }
            }
        }
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
        Vector2 enemyCenter = new Vector2();
        this.sprite.getBoundingRectangle().getCenter(enemyCenter);
        // deals damage to player just once.
        damageDealingLogic(player, playerCenter, enemyCenter);
        // standing still position is down-facing
        String animationMapQuery = "down";
        // animate enemy direction movement correctly
        if(followingPlayer){
            if(Math.abs(enemyCenter.y-playerCenter.y) > Math.abs(enemyCenter.x - playerCenter.x)){
                if(enemyCenter.y > playerCenter.y){
                    animationMapQuery = "down";
                }
                else {
                    animationMapQuery = "up";
                }
            }
            else if(Math.abs(enemyCenter.y-playerCenter.y) < Math.abs(enemyCenter.x - playerCenter.x)){
                if(enemyCenter.x > playerCenter.x){
                    animationMapQuery = "left";
                }
                else {
                    animationMapQuery = "right";
                }
            }
            // get frame from animation and draw at the correct coordinates,
            // since the sprites of the animation do not contain the correct coordinates.
            Sprite enemyMovementSprite = this.animationMap.get(animationMapQuery).getKeyFrame(stateTime, true);
            enemyMovementSprite.setPosition(this.sprite.getX(), this.sprite.getY());
            enemyMovementSprite.draw(spriteBatch);

        }
        else{
            this.sprite.draw(spriteBatch);
        }

    }

    /**
     * Both receiving logic and giving out as well.
     * @param player
     * @param playerCenter
     * @param enemyCenter
     */
    public void playerAttack(Player player, Vector2 playerCenter, Vector2 enemyCenter) {
        if (player.currentState.contains("attacking")) {
            if (player.currentState.contains("left") && playerCenter.x >= enemyCenter.x) {
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
            if (player.currentState.contains("down") && playerCenter.y >= enemyCenter.y) {
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

        // do not follow breadcrumb if there are walls between enemy and breadcrumb
        Array<Vector2> pointsBetween = new Array<>(Vector2.class);

        Vector2 enemyPosition = new Vector2(this.sprite.getX(), this.sprite.getY());
        Vector2 breadCrumbPosition = new Vector2(breadCrumb.sprite.getX(), breadCrumb.sprite.getY());
        Vector2 sum = new Vector2(enemyPosition.x + breadCrumbPosition.x, enemyPosition.y+ breadCrumbPosition.y);
        Vector2 middle = new Vector2(sum.x/2, sum.y/2);
        Vector2 pointBetweenEnemyAndMiddle = new Vector2((enemyPosition.x+middle.x)/2,
                (enemyPosition.y + middle.y)/2);
        Vector2 pointBetweenMiddleAndBreadCrumb = new Vector2((breadCrumbPosition.x+middle.x)/2,
                (breadCrumbPosition.y + middle.y)/2);

        //pointsBetween.add(enemyPosition);
        pointsBetween.add(pointBetweenEnemyAndMiddle);
        pointsBetween.add(middle);
        pointsBetween.add(pointBetweenMiddleAndBreadCrumb);
        pointsBetween.add(breadCrumbPosition);

        for (Vector2 pointBetween : pointsBetween){
            for (GameObject gameObject : this.walls){
                if (gameObject.sprite.getBoundingRectangle().contains(pointBetween)){
                    return;
                }
            }
        }

        float xDifference = breadCrumb.sprite.getX() - this.sprite.getX();
        float yDifference = breadCrumb.sprite.getY() - this.sprite.getY();

        if(xDifference < 0){
            this.sprite.translateX(-this.SPEED);
            this.center.add(-this.SPEED, 0);
        }
        if(xDifference > 0){
            this.sprite.translateX(this.SPEED);
            this.center.add(this.SPEED, 0);
        }
        if(yDifference < 0){
            this.sprite.translateY(-this.SPEED);
            this.center.add(0, -this.SPEED);
        }
        if(yDifference > 0){
            this.sprite.translateY(this.SPEED);
            this.center.add(0, this.SPEED);
        }

        this.attackBox.setPosition(this.center);
        this.hitBox.setPosition(this.center);
        this.followBox.setPosition(this.center);
    }

}
