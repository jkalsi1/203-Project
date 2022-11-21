import processing.core.PImage;

import java.util.List;

public class TREE extends Plant
{
    public static final String TREE_KEY = "tree";
    private static final int TREE_NUM_PROPERTIES = 7;
    private static final int TREE_ID = 1;
    private static final int TREE_COL = 2;
    private static final int TREE_ROW = 3;
    private static final int TREE_ANIMATION_PERIOD = 4;
    private static final int TREE_ACTION_PERIOD = 5;
    private static final int TREE_HEALTH = 6;
    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    public TREE(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public static int getTreeActionMax() {
        return TREE_ACTION_MAX;
    }

    public static int getTreeActionMin() {
        return TREE_ACTION_MIN;
    }


    public static int getTreeAnimationMax() {
        return TREE_ANIMATION_MAX;
    }

    public static int getTreeAnimationMin() {
        return TREE_ANIMATION_MIN;
    }


    public static int getTreeHealthMax() {
        return TREE_HEALTH_MAX;
    }

    public static int getTreeHealthMin() {
        return TREE_HEALTH_MIN;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            STUMP stump = new STUMP( this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(STUMP.STUMP_KEY), 0, 0);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public static boolean parseTree(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            TREE entity = new TREE(properties[TREE_ID],
                    pt,
                    imageStore.getImageList(TREE_KEY),
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    0);
            world.tryAddEntity(entity);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {

                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        this.getActionPeriod());
               scheduler.scheduleEvent(this,
                        new Animation(this, null, null, 0),
                        this.getAnimationPeriod());

    }
}
