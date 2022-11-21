import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class SAPLING extends Plant
{

    public static final String SAPLING_KEY = "sapling";
    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    private static final int SAPLING_NUM_PROPERTIES = 4;
    private static final int SAPLING_ID = 1;
    private static final int SAPLING_COL = 2;
    private static final int SAPLING_ROW = 3;
    private static final int SAPLING_HEALTH = 4;

    public SAPLING(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super( id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    // health starts at 0 and builds up until ready to convert to Tree
//    public static Sapling createSapling(
//            String id,
//            Point position,
//            List<PImage> images) {
//        return new Sapling("SAPLING", id, position, images,
//                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
//    }


    public static int getSaplingActionAnimationPeriod() {
        return SAPLING_ACTION_ANIMATION_PERIOD;
    }

    public static int getSaplingHealthLimit() {
        return SAPLING_HEALTH_LIMIT;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        this.setHealth(this.getHealth()+1);
        if (!this.transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            STUMP stump = new STUMP(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(STUMP.STUMP_KEY), 0, 0);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        } else if (this.getHealth() >= this.getHealthLimit()) {
            TREE tree = new TREE("tree_" + this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(TREE.TREE_KEY),
                    SAPLING.getNumFromRange(TREE.getTreeActionMax(), TREE.getTreeActionMin()),
                    SAPLING.getNumFromRange(TREE.getTreeAnimationMax(), TREE.getTreeAnimationMin()),
                    SAPLING.getNumFromRange(TREE.getTreeHealthMax(), TREE.getTreeHealthMin()),
                    0);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(VirtualWorld.getScheduler(), world, imageStore);

            return true;
        }

        return false;
    }

    public static boolean parseSapling(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            SAPLING entity = new SAPLING( id, pt, imageStore.getImageList(SAPLING_KEY),
                    SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health, SAPLING_HEALTH_LIMIT);
            world.tryAddEntity(entity);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation(this, null, null, 0),
                this.getAnimationPeriod());

    }

    public static int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }
}
