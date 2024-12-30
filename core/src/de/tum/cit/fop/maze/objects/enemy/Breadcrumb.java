package de.tum.cit.fop.maze.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.objects.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for markers left by player
 */
public class Breadcrumb {

    public static List<Breadcrumb> allPlayerBreadCrumbs = new ArrayList<>();

    public Sprite sprite;
    private Instant creationTime;
    private Player player;

    public Breadcrumb(Player player){
        this.player = player;
        // create breadcrumb at given location, but not to be drawn.
        Texture texture = new Texture(Gdx.files.internal("character_white.png"));
        TextureRegion textureRegion = new TextureRegion(texture, 3, 3);
        this.sprite = new Sprite(textureRegion);
        this.sprite.setSize(3, 3);
        this.sprite.setPosition(player.sprite.getX(), player.sprite.getY());

        creationTime = Instant.now();

        allPlayerBreadCrumbs.add(this);
    }

    private Breadcrumb(){}


    public static void removeOutDatedBreadcrumbs(){
        List<Integer> indicesOfBreadCrumbsToBeRemoved = new ArrayList<>();

        for (int i = 0; i < allPlayerBreadCrumbs.size(); i++){
            if(allPlayerBreadCrumbs.get(i).isExpired()){
                indicesOfBreadCrumbsToBeRemoved.add(i);
            }
        }

        for (int i : indicesOfBreadCrumbsToBeRemoved ){
            allPlayerBreadCrumbs.remove(i);
            allPlayerBreadCrumbs.add(i, new Breadcrumb());
        }
        // remove placeholder breadcrumbs
        allPlayerBreadCrumbs.removeIf(b -> b.sprite == null);
    }

    private boolean isExpired(){
        Instant currentTime = Instant.now();
        long timeElapsed = Duration.between(this.creationTime, currentTime).toSeconds();
        return timeElapsed >=2;
    }
}
