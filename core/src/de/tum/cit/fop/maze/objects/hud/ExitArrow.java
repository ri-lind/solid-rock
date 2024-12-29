package de.tum.cit.fop.maze.objects.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Exit;
import de.tum.cit.fop.maze.objects.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ExitArrow {


    Player player;

    public Sprite sprite;
    public ExitArrow(List<Exit> exits, Player player) {

        this.player = player;
        Texture texture = new Texture(Gdx.files.internal("arrow.png"));
        Sprite sprite = new Sprite(texture);
        sprite.setSize(10, 10);
        sprite.setX(player.sprite.getX());
        sprite.translateX(sprite.getWidth());
        sprite.setY(player.sprite.getY());
        sprite.translateY(player.sprite.getHeight());
        System.out.println(sprite.getWidth());
        sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);

        //sprite.setRotation(180);
        this.sprite = sprite;

        pointToNearest(exits);
    }

    private void pointTo(List<Exit> exit){

        // two functions
        // 1. find nearest
        // point to nearest
        //
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
        this.player.sprite.getBoundingRectangle().getCenter(exitCenter);

        return playerCenter.dst(exitCenter);
    }


}
