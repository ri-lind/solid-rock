package de.tum.cit.fop.maze.utilities;

import com.badlogic.gdx.math.Vector2;

public class MathUtilities {


    private static float dotProduct(Vector2 vector1, Vector2 vector2){
        return vector1.x * vector2.x + vector1.y *vector2.y;
    }

    private static float magnitude(Vector2 vector){
        return (float) Math.sqrt(vector.x* vector.x + vector.y * vector.y);
    }
    public static float angleBetween(Vector2 vector1, Vector2 vector2){
        float dotProduct = dotProduct(vector1, vector2);
        float magnitudeFirst = magnitude(vector1);
        float magnitudeSecond = magnitude(vector2);

        double angleInRadians = Math.acos(dotProduct/(magnitudeFirst*magnitudeSecond));
        return (float) Math.toDegrees(angleInRadians);
    }
}
