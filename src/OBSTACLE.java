import processing.core.PImage;

import java.util.List;

public class OBSTACLE extends AnimatedEntity
{
    public static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_NUM_PROPERTIES = 5;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;
    private static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public OBSTACLE(String id, Point position, List<PImage> images, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, animationPeriod, health, healthLimit);
    }

//    public static Obstacle createObstacle(
//            String id, Point position, int animationPeriod, List<PImage> images)
//    {
//        return new Obstacle("OBSTACLE", id, position, images,
//                animationPeriod, 0, 0);
//    }

    public static boolean parseObstacle(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            OBSTACLE entity = new OBSTACLE(properties[OBSTACLE_ID], pt,
                    imageStore.getImageList(
                            OBSTACLE_KEY),
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    0, 0
                    );
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {


                scheduler.scheduleEvent(this,
                        new Animation(this, null, null, 0),
                        this.getAnimationPeriod());

    }
}
