package de.tum.cit.fop.maze.objects.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class for markers left by player
 */
public class Breadcrumb {

    public static List<Breadcrumb> allPlayerBreadCrumbs = new ArrayList<>();

    public Sprite sprite;
    public Instant creationTime;

    public Breadcrumb(Player player){
        Vector2 playerCenter = new Vector2();
        player.sprite.getBoundingRectangle().getCenter(playerCenter);
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
        long timeElapsed = Duration.between(this.creationTime, currentTime).toMillis();
        return timeElapsed >= 1000;
    }

    public static Optional<Breadcrumb> calculateFarthestInRange(Enemy enemy){
        Sprite enemySprite = enemy.sprite;
        Vector2 enemyLocation = new Vector2();
        enemySprite.getBoundingRectangle().getCenter(enemyLocation);

        AtomicReference<Float> maxDistance = new AtomicReference<>((float) 0);
        AtomicReference<Breadcrumb> breadcrumbAtomicReference = new AtomicReference<>();

       allPlayerBreadCrumbs.forEach(
               breadcrumb -> {
                   Vector2 breadcrumbCenter = new Vector2();
                   breadcrumb.sprite.getBoundingRectangle().getCenter(breadcrumbCenter);
                   float distance = enemyLocation.dst(breadcrumbCenter);
                   if( distance > maxDistance.get() && enemy.followBox.contains(breadcrumbCenter)){
                       maxDistance.set(distance);
                       breadcrumbAtomicReference.set(breadcrumb);
                   }
               }
       );

       // maybe we can use this somehow.
       float furthestDistance = maxDistance.get();

       return Optional.ofNullable(breadcrumbAtomicReference.get());

    }
}
