package de.tum.cit.fop.maze.utilities.level;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import de.tum.cit.fop.maze.objects.BorderWall;
import de.tum.cit.fop.maze.objects.GameObject;
import de.tum.cit.fop.maze.objects.Level;
import de.tum.cit.fop.maze.objects.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
        GameObject closestWallOrEmptyWall = this.getWallInTheVicinity(x, y);

        float xCoordinate = x;
        float yCoordinate = y;
        if (closestWallOrEmptyWall.sprite.getX() != 0 && closestWallOrEmptyWall.sprite.getY() != 0) {

            GameObject closestWall = closestWallOrEmptyWall;

            xCoordinate = closestWall.sprite.getX();
            yCoordinate = closestWall.sprite.getY();

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
             // adds it to the objects to be drawn
        }

        // add wall with the given coordinates to the game objects.
        Wall wall = new Wall(xCoordinate, yCoordinate);
        wallsToBeSpawned.add(wall);
        this.level.gameObjects.get(0).add(wall);
    }


    public GameObject getWallInTheVicinity(float x, float y){
        Circle vicinityCircle = this.createCircle(x, y);
        final Vector2[] closestWallPosition = {new Vector2(0, 0)};
        AtomicInteger objectType = new AtomicInteger(); // defaults to wall
        for (int i : new int[]{0, 6}){
            this.level.gameObjects.get(i).forEach(
                    (wall) -> {
                        if (vicinityCircle.contains(wall.sprite.getX() + wall.sprite.getWidth()/2,
                                wall.sprite.getY() + wall.sprite.getHeight()/2)){
                            Vector2 loopWallPosition = new Vector2(wall.sprite.getX(), wall.sprite.getY());
                            Vector2 temporaryWallPosition = new Vector2(x, y);
                            float distanceToNearWall = loopWallPosition.dst(temporaryWallPosition);
                            float distanceToClosestWall = loopWallPosition.dst(closestWallPosition[0]);
                            if(distanceToNearWall < distanceToClosestWall){
                                closestWallPosition[0] = new Vector2(wall.sprite.getX(), wall.sprite.getY());
                                objectType.set(i);
                            }
                        }
                    }
            );
        }
        if (objectType.get() == 0) return new Wall(closestWallPosition[0].x, closestWallPosition[0].y);
        else return new BorderWall(closestWallPosition[0].x, closestWallPosition[0].y);
    }



    public Circle createCircle(float x, float y){
        Circle circle = new Circle();
        circle.setPosition(x , y);
        circle.setRadius(30f);

        return circle;

    }
}
