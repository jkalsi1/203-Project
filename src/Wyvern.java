import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

// It extends ActionEntities since we want this to have an action (like wyvern is the "infected entity" and it chases dudes)
public class Wyvern  extends ActionEntities{

    // we use this key for parsing and to map wyvern entity to its image list
    public static final String WYVERN_KEY = "wyvern";
    private static final int WYVERN_NUM_PROPERTIES = 6;
    private static final int WYVERN_ID = 1;
    // I think these col rows determine size of the entity pic? on map?
    private static final int WYVERN_COL = 2;
    private static final int WYVERN_ROW = 3;
    private static final int WYVERN_ACTION_PERIOD = 4;
    // I think this determines how fast the animation is (like 5 ticks per sec in this case or sth)
    private static final int WYVERN_ANIMATION_PERIOD = 5;
    public Wyvern(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    // creates new wyvern at beginning of world
    public static boolean parseWyvern(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == WYVERN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[WYVERN_COL]),
                    Integer.parseInt(properties[WYVERN_ROW]));
            Wyvern entity = new Wyvern(properties[WYVERN_ID],
                    pt,
                    // this line below gets the list of images corresponding to the key we have, in this case our wyvern key (check imagelist.txt)
                    imageStore.getImageList(WYVERN_KEY),

                    Integer.parseInt(properties[WYVERN_ACTION_PERIOD]),
                    // I think this determines how fast the animation goes
                    Integer.parseInt(properties[WYVERN_ANIMATION_PERIOD]),
                    0, 0
            );
            world.tryAddEntity(entity);
        }

        return properties.length == WYVERN_NUM_PROPERTIES;
    }

    // movement strategy for wyvern, for now it will copy the fairy's movement just to test if this works
    public static boolean moveToWyvern(
            Wyvern wyvern,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(wyvern.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {

            Point nextPos = wyvern.nextPositionWyvern(world, target.getPosition());

            if (!wyvern.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(wyvern, nextPos);
            }

            return false;
        }
    }

    // calculates the next pos that wyvern takes, it will copy fairy movement just to check if scheduling actions works
    private Point nextPositionWyvern(WorldModel world, Point destPos)
    {
        // creating new astar class
        AStarPathingStrategy wyvernStep = new AStarPathingStrategy();
        // predicate to check if the point can be passed through
        Predicate<Point> pred = s -> !world.isOccupied(s) && world.withinBounds(s);

        // checks if two points are adjacent to each other (withinReach check)
        BiPredicate<Point, Point> bipred = (p1, p2) -> Point.adjacent(p1, p2);

        // creates list of points that the wyvern can take (AStar path)
        List<Point> wyvernPath = wyvernStep.computePath(this.getPosition(), destPos,
                pred, bipred, wyvernStep.CARDINAL_NEIGHBORS);

        // if there is a non-empty list (meaning theres a path to be taken)
        if (!wyvernPath.isEmpty()) {
            // new point is index 0 of the path since youre calling this every time so you will get most optimal single step each time
            Point newPos = new Point(wyvernPath.get(0).x, wyvernPath.get(0).y);
            return newPos;
        }
        else
        {
            // if there is no path (empty, example could be when the entity is blocked on all sides)
            // keep the same position
            Point newPos = new Point(this.getPosition().x, this.getPosition().y);
            return newPos;
        }
    }

    // this activity mimics the fairy activity just to see if scheduling actions works, we will change this later to whatever
    // activity we want it to be
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
    // grabs the stump as target, we just need to change the STUMP.class, in array list parameter below, to DUDE.class
        // if we want this wyvern to chase dudes, but we want to slow down their movement speed so that they dont immediately
        // catch up to the dudes, maybe like 3 times slower, not sure, lots of issues arise here that we need to think of
        Optional<Entity> wyvernTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(STUMP.class)));

        if (wyvernTarget.isPresent()) {
            Point tgtPos = wyvernTarget.get().getPosition();

            if (Wyvern.moveToWyvern( this, world, wyvernTarget.get(), scheduler)) {
                SAPLING sapling = new SAPLING("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(SAPLING.SAPLING_KEY), SAPLING.getSaplingActionAnimationPeriod(), SAPLING.getSaplingActionAnimationPeriod(), 0, SAPLING.getSaplingHealthLimit());

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
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
// creates activity upon entity creation
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
// creates animation upon entity creation
        scheduler.scheduleEvent(this,
                new Animation(this, null, null, 0),
                this.getAnimationPeriod());

    }
}
