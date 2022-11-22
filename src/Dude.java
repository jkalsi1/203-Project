import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Dude extends ActionEntities
{
    public static final String DUDE_KEY = "dude";
    private static final int DUDE_NUM_PROPERTIES = 7;
    private static final int DUDE_ID = 1;
    private static final int DUDE_COL = 2;
    private static final int DUDE_ROW = 3;
    private static final int DUDE_LIMIT = 4;
    private static final int DUDE_ACTION_PERIOD = 5;
    private static final int DUDE_ANIMATION_PERIOD = 6;

    private int resourceLimit;
    private int resourceCount;

    public Dude(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    @Override
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

    public static boolean parseDude(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            DUDE_NOT_FULL entity = new DUDE_NOT_FULL( properties[DUDE_ID],
                    pt,
                    imageStore.getImageList(DUDE_KEY),
                    Integer.parseInt(properties[DUDE_LIMIT]),
                    0,
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    10 , 10);
            world.tryAddEntity(entity);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }

    public Point nextPositionDude(WorldModel world, Point destPos)
    {
        AStarPathingStrategy dudeStep = new AStarPathingStrategy();

        Predicate<Point> pred = s -> !world.isOccupied(s) && world.withinBounds(s);

        // ignore for now, single step doesn't check this
        BiPredicate<Point, Point> bipred = (p1, p2) -> Point.adjacent(p1, p2);

        List<Point> DudePath = dudeStep.computePath(this.getPosition(), destPos,
                pred, bipred, dudeStep.CARDINAL_NEIGHBORS);
        //int horiz = Integer.signum(destPos.x - this.getPosition().x);

        if(!DudePath.isEmpty())
        {
            Point newPos = new Point(DudePath.get(0).x, DudePath.get(0).y);
            return newPos;
        }
        else
        {
            Point newPos = new Point(this.getPosition().x, this.getPosition().y);
            return newPos;
        }

//        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof STUMP)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) &&  !(world.getOccupancyCell(newPos) instanceof STUMP)) {
//                newPos = this.getPosition();
//            }
//        }
    }

    public abstract boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);
}
