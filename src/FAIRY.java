import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FAIRY extends ActionEntities
{
    public static final String FAIRY_KEY = "fairy";
    private static final int FAIRY_NUM_PROPERTIES = 6;
    private static final int FAIRY_ID = 1;
    private static final int FAIRY_COL = 2;
    private static final int FAIRY_ROW = 3;
    private static final int FAIRY_ANIMATION_PERIOD = 4;
    private static final int FAIRY_ACTION_PERIOD = 5;

    public FAIRY(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super( id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }




    public static boolean moveToFairy(
            FAIRY fairy,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(fairy.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {

            Point nextPos = fairy.nextPositionFairy(world, target.getPosition());

            if (!fairy.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(fairy, nextPos);
            }

            return false;
        }
    }

    private Point nextPositionFairy(WorldModel world, Point destPos)
    {
        AStarPathingStrategy fairyStep = new AStarPathingStrategy();

        Predicate<Point> pred = s -> !world.isOccupied(s) && world.withinBounds(s);

        // ignore for now, single step doesn't check this
        BiPredicate<Point, Point> bipred = (p1, p2) -> Point.adjacent(p1, p2);

        List<Point> FairyPath = fairyStep.computePath(this.getPosition(), destPos,
                pred, bipred, fairyStep.CARDINAL_NEIGHBORS);

        if (!FairyPath.isEmpty()) {
            Point newPos = new Point(FairyPath.get(0).x, FairyPath.get(0).y);
            return newPos;
        }
        else
        {
            Point newPos = new Point(this.getPosition().x, this.getPosition().y);
            return newPos;
         }
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = this.getPosition();
//            }
//        }
//
//       return newPos;
    }

    public static boolean parseFairy(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            FAIRY entity = new FAIRY(properties[FAIRY_ID],
                    pt,
                    imageStore.getImageList(FAIRY_KEY),
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    0, 0
                    );
            world.tryAddEntity(entity);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        Optional<Entity> fairyTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(STUMP.class, DeadDude.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (fairyTarget.get().getClass().equals(STUMP.class))
            {
                if (FAIRY.moveToFairy(this, world, fairyTarget.get(), scheduler)) {
                    SAPLING sapling = new SAPLING("sapling_" + this.getId(), tgtPos,
                            imageStore.getImageList(SAPLING.SAPLING_KEY), SAPLING.getSaplingActionAnimationPeriod(), SAPLING.getSaplingActionAnimationPeriod(), 0, SAPLING.getSaplingHealthLimit());

                    world.addEntity(sapling);
                    sapling.scheduleActions(scheduler, world, imageStore);
                }
            }
            if (fairyTarget.get().getClass().equals(DeadDude.class))
            {
                if (FAIRY.moveToFairy(this, world, fairyTarget.get(), scheduler))
                {
                    DUDE_NOT_FULL entity = new DUDE_NOT_FULL( fairyTarget.get().getId(),
                            tgtPos,
                            imageStore.getImageList(Dude.DUDE_KEY),
                            Dude.getDudeLimit(),
                            0,
                            Dude.getDudeActionPeriod(),
                            Dude.getDudeAnimationPeriod(),
                            10 , 10);
                    world.removeEntity(fairyTarget.get());
                    world.addEntity(entity);
                    System.out.println("gg");
                    entity.scheduleActions(scheduler, world, imageStore);
                }
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
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
