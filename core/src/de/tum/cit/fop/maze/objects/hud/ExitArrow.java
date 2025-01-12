package de.tum.cit.fop.maze.objects.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Exit;
import de.tum.cit.fop.maze.objects.Player;
import de.tum.cit.fop.maze.utilities.MathUtilities;

import java.util.List;
public class ExitArrow {


    Player player;
    Vector2 currentDirection;
    List<Exit> exits;

    public Sprite sprite;
    public ExitArrow(List<Exit> exits, Player player) {

        this.player = player;
        this.exits = exits;
        this.currentDirection = new Vector2(1,0);

        Texture texture = new Texture(Gdx.files.internal("arrow.png"));
        Sprite sprite = new Sprite(texture);
        sprite.setSize(10, 10);
        sprite.setX(player.sprite.getX());
        sprite.translateX(player.sprite.getWidth());
        sprite.setY(player.sprite.getY());
        sprite.translateY(player.sprite.getHeight());
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
        sprite.setRotation(180);
        this.sprite = sprite;
        pointToNearest();
    }

    public void pointToNearest(){

        // two functions
        // 1. find nearest
        // point to nearest
        //
        Exit nearestExit = findNearest(this.exits);

        pointTo(nearestExit);
    }

    private Exit findNearest(List<Exit> exits) {

        Vector2 playerCenter = new Vector2();
        this.player.sprite.getBoundingRectangle().getCenter(playerCenter);

        float minimalDistance = euclidianDistanceTo(exits.get(0));
        Exit nearestExit = exits.get(0);

        for (Exit e : exits){
            float distance = euclidianDistanceTo(e);
            if(distance < minimalDistance){
                minimalDistance = distance;
                nearestExit = e;
            }
        }
        return nearestExit;
    }

    private float euclidianDistanceTo(Exit exit){
        Vector2 playerCenter = new Vector2();
        this.player.sprite.getBoundingRectangle().getCenter(playerCenter);

        Vector2 exitCenter = new Vector2();
        exit.sprite.getBoundingRectangle().getCenter(exitCenter);

        return playerCenter.dst(exitCenter);
    }

    private void pointTo(Exit exit){
        // will need to rotate the sprite based on the exit.
        // sprite position horizontal
        RightDirection rightDirection = calculateDirectionToExit(exit);
        Vector2 vectorToExit = rightDirection.rightDirectionVector() ;

        float angle = MathUtilities.angleBetween(this.currentDirection, vectorToExit);
        angle+= 180 * rightDirection.isExitAbove;

        this.sprite.setRotation(angle);

    }

    private RightDirection calculateDirectionToExit(Exit exit){
        Vector2 playerCenter = new Vector2();
        this.player.sprite.getBoundingRectangle().getCenter(playerCenter);

        Vector2 exitCenter = new Vector2();
        exit.sprite.getBoundingRectangle().getCenter(exitCenter);

        return new RightDirection(playerCenter, exitCenter);
    }

}
