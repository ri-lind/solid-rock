package de.tum.cit.fop.maze.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import de.tum.cit.fop.maze.objects.enemy.Breadcrumb;
import de.tum.cit.fop.maze.objects.hud.ExitArrow;
import de.tum.cit.fop.maze.objects.hud.Heart;
import de.tum.cit.fop.maze.utilities.LoaderHelper;

import java.time.Duration;
import java.time.Instant;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Represents a player, in the sense that it contains the animations, coordinates and has some manipulation
 * logic in it. The focal center is the currentState attribute, which is heavily manipulated by this
 * class and the method InputHandler.input().
 */
public class Player {
    public Dictionary<String, Animation<Sprite>> animations;
    // the animations dictionary and calculate next move methods depend on this field
    public String currentState = "";
    public Sprite sprite;
    private float SPEED = 1f;
    public int attackFramesCounter = 0;

    public Heart heart;
    public ExitArrow exitArrow;


    public int keysInPosession;

    /**
     * Used for normal visible player.
     */
    public Player(){

        this.animations = new Hashtable<>();
        LoaderHelper.loadCharacterDirectionAnimation(this); // Load character movement and standing animation
        LoaderHelper.loadCharacterAttackAnimations(this); // load the attack animations of the character
        this.currentState = "RIGHT";
        this.sprite.setX(600);
        this.sprite.setY(400);
        this.sprite.setSize(32, 32);

        this.heart = new Heart(this);
        this.keysInPosession = 0;
    }

    /**
     * Constructor used for the temporary player.
     * @param otherPlayer
     */
    public Player(Player otherPlayer){
        this.animations = new Hashtable<>();
        this.currentState = otherPlayer.currentState;
        // not really needed
        this.SPEED = otherPlayer.SPEED;
        this.attackFramesCounter = otherPlayer.attackFramesCounter;
        // needed
        LoaderHelper.loadCharacterDirectionAnimation(this);
        this.sprite.setBounds(otherPlayer.sprite.getX(), otherPlayer.sprite.getY(), otherPlayer.sprite.getRegionWidth(), otherPlayer.sprite.getRegionHeight());
        this.heart = new Heart(this);
    }


    // think about the objects the player cannot overlap with
    public void calculateNextMove(boolean truePlayer){
        if(truePlayer){
            this.exitArrow.pointToNearest();
            // leave invisible breadcrumb, add this to player and remove outdated ones.
            if(Breadcrumb.allPlayerBreadCrumbs.isEmpty()){
                new Breadcrumb(this);
            } else if(Duration.between(Breadcrumb.allPlayerBreadCrumbs.get(Breadcrumb.allPlayerBreadCrumbs.size()-1).creationTime, Instant.now()).toSeconds() > 2){
                new Breadcrumb(this);
            }

        }
        if (currentState.toLowerCase().contains("running")){
            SPEED = 2 * SPEED; // running is double the speed
        }
        if ("down".contains(currentState.toLowerCase()) || currentState.toLowerCase().contains("down-running")){
            this.sprite.translateY(-SPEED);
            if(truePlayer){
                this.heart.sprite.translateY(-SPEED);
                this.exitArrow.sprite.translateY(-SPEED);
            }
        } else if("right".contains(currentState.toLowerCase()) ||
                currentState.toLowerCase().contains("right-running")) { // right
            this.sprite.translateX(SPEED);
            if (truePlayer){
                this.heart.sprite.translateX(SPEED);
                this.exitArrow.sprite.translateX(SPEED);
            }
        } else if ("up".contains(currentState.toLowerCase()) ||
                currentState.toLowerCase().contains("up-running")) {// up
            this.sprite.translateY(SPEED);
            if ( truePlayer) {
                this.heart.sprite.translateY(SPEED);
                this.exitArrow.sprite.translateY(SPEED);
            }
        } else if ("left".contains(currentState.toLowerCase()) ||
                currentState.toLowerCase().contains("left-running")) {// left
            this.sprite.translateX(-SPEED);
            if(truePlayer){
                this.heart.sprite.translateX(-SPEED);
                this.exitArrow.sprite.translateX(-SPEED);

            }
        }
         SPEED = 1f; // reset speed after movement
    }

    public Animation<Sprite> getCurrentAnimation(){
        return this.animations.get(currentState.toLowerCase().replace("-running", ""));
    }
}