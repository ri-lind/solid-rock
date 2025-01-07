package de.tum.cit.fop.maze.utilities.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Wall;

import java.util.ArrayList;
import java.util.List;


public class MapCreator {

    private Level level;
    private List<Wall> wallsToBeSpawned;
    public MapCreator(Level level){
        this.level = level;
        this.wallsToBeSpawned = new ArrayList<>();
    }

    // logic of this class
    /**
     * Takes mouse-click at given coordinate.
     * Checks whether there is a wall in the vicinity and if so, appends the wall to that wall perpendicularly.
     * If not it spawns the wall at the given coordinate.
     * Pressing the button Q in the keyboard takes the given objects with the given coordinates and writes a java
     * property file.
     */

    /**
     * Triggers on mouse click
     */
    public void spawnAtGivenCoordinates(float x, float y) {
        Wall closestWallOrEmptyWall = this.getWallInTheVicinity(x, y);

        float xCoordinate = x;
        float yCoordinate = y;
        if (closestWallOrEmptyWall.sprite.getX() == 0 && closestWallOrEmptyWall.sprite.getY() == 0) {
            // no closest wall

        } else {
            Wall closestWall = closestWallOrEmptyWall;
            float xDifference = x - closestWall.sprite.getX();
            float yDifference = y - closestWall.sprite.getY();

            if (Math.abs(yDifference) < Math.abs(xDifference)) {
                if (xDifference < 0) {
                    xCoordinate = closestWall.sprite.getX() - closestWall.sprite.getWidth();
                } else {
                    xCoordinate = closestWall.sprite.getX() + closestWall.sprite.getWidth();
                }
            } else {
                if(yDifference < 0){
                    yCoordinate = closestWall.sprite.getY() - closestWall.sprite.getHeight();
                } else {
                    yCoordinate = closestWall.sprite.getY() + closestWall.sprite.getHeight();
                }
            }
            Wall wall = new Wall(xCoordinate, yCoordinate);
            wallsToBeSpawned.add(wall);
            this.level.gameObjects.get(0).add(wall); // adds it to the objects to be drawn
        }
    }


    public Wall getWallInTheVicinity(float x, float y){
        Wall temporaryWall = new Wall(x,y);
        Circle vicinityCircle = new Circle();
        vicinityCircle.setPosition(x + temporaryWall.sprite.getWidth()/2, y + temporaryWall.sprite.getHeight()/2);
        vicinityCircle.setRadius(30f);
        final Wall[] closestWall = {new Wall(0, 0)};
        final Vector2[] closestWallPosition = {new Vector2(closestWall[0].sprite.getX(), closestWall[0].sprite.getY())};

        this.level.gameObjects.get(0).forEach(
                (wall) -> {
                    if (vicinityCircle.contains(wall.sprite.getX() + wall.sprite.getWidth()/2,
                            wall.sprite.getY() + wall.sprite.getHeight()/2)){
                        Vector2 loopWallPosition = new Vector2(wall.sprite.getX(), wall.sprite.getY());
                        Vector2 temporaryWallPosition = new Vector2(temporaryWall.sprite.getX(), temporaryWall.sprite.getY());
                        float distanceToNearWall = loopWallPosition.dst(temporaryWallPosition);
                        float distanceToClosestWall = loopWallPosition.dst(closestWallPosition[0]);
                        if(distanceToNearWall < distanceToClosestWall){
                            closestWall[0] = new Wall(wall.sprite.getX(), wall.sprite.getY());
                            closestWallPosition[0] = new Vector2(wall.sprite.getX(), wall.sprite.getY());
                        }
                    }
                }
        );

        return closestWall[0];
    }
}
