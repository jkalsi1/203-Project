import processing.core.PImage;

import java.util.List;

public class Wyvern  extends ActionEntities{

    public static final String WYVERN_KEY = "wyvern";
    private static final int WYVERN_NUM_PROPERTIES = 6;
    private static final int WYVERN_ID = 1;
    private static final int WYVERN_COL = 2;
    private static final int WYVERN_ROW = 3;
    private static final int WYVERN_ACTION_PERIOD = 4;
    private static final int WYVERN_ANIMATION_PERIOD = 5;
    public Wyvern(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static boolean parseWyvern(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == WYVERN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[WYVERN_COL]),
                    Integer.parseInt(properties[WYVERN_ROW]));
            Wyvern entity = new Wyvern(properties[WYVERN_ID],
                    pt,
                    imageStore.getImageList(WYVERN_KEY),
                    Integer.parseInt(properties[WYVERN_ACTION_PERIOD]),
                    Integer.parseInt(properties[WYVERN_ANIMATION_PERIOD]),
                    0, 0
            );
            world.tryAddEntity(entity);
        }

        return properties.length == WYVERN_NUM_PROPERTIES;
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

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
//    public Wyvern(String id, Point position, List<PImage> images, int animationPeriod, int health, int healthLimit) {
//        super(id, position, images, animationPeriod, health, healthLimit);
//    }
//
////    public static boolean parseWyvern(
////            String[] properties, WorldModel world, ImageStore imageStore)
////    {
////        if (properties.length == WYVERN_NUM_PROPERTIES) {
////            Point pt = new Point(Integer.parseInt(properties[WYVERN_COL]),
////                    Integer.parseInt(properties[WYVERN_ROW]));
////            OBSTACLE entity = new OBSTACLE(properties[WYVERN_ID], pt,
////                    imageStore.getImageList(
////                            WYVERN_KEY),
////                    Integer.parseInt(properties[WYVERN_ANIMATION_PERIOD]),
////                    0, 0
////            );
////            world.tryAddEntity(entity);
////        }
////
////        return properties.length == WYVERN_NUM_PROPERTIES;
////    }
}
