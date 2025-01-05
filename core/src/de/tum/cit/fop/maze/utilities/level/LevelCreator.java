package de.tum.cit.fop.maze.utilities.level;

public class LevelCreator {

    private final int level_number;

    private final static int WORLD_WIDTH = 800;
    private final static int WORLD_HEIGHT = 500;

    public LevelCreator(int level_number){
        this.level_number = level_number;
    }

    public static String createLevel(){
        return createLevel5();
    }


    private static String createLevel5(){
        StringBuilder level1 = new StringBuilder();
        // create walls at height 100, 200, 300, 400

        for (int height = 0; height <= 32; height +=8){
            for (int x = 0; x <= 50; x++){
                level1.append(x).append(",").append(height).append("=0").append("\n");
            }
        }
        level1.append(10).append(",").append(10).append("=1").append("\n");
        return level1.toString();

    }
}
