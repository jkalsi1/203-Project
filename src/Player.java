import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Player extends ActionEntities {
    // we use this key for parsing and to map player entity to its image list
    public static final String PLAYER_KEY = "player";
    private static final int PLAYER_NUM_PROPERTIES = 6;
    private static final int PLAYER_ID = 1;
    // I think these col rows determine size of the entity pic? on map?
    private static final int PLAYER_COL = 2;
    private static final int PLAYER_ROW = 3;
    private static final int PLAYER_ACTION_PERIOD = 1;
    // I think this determines how fast the animation is (like 5 ticks per sec in this case or sth)
    private static final int PLAYER_ANIMATION_PERIOD = 5;

    public Player(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    // creates new player at beginning of world
    public static boolean parsePlayer(
            String[] properties, WorldModel world, ImageStore imageStore)
    {
        if (properties.length == PLAYER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[PLAYER_COL]),
                    Integer.parseInt(properties[PLAYER_ROW]));
            System.out.println("player id: ");
            System.out.println(properties[PLAYER_ID]);

            Player entity = new Player(properties[PLAYER_ID],
                    pt,
                    // this line below gets the list of images corresponding to the key we have, in this case our player key (check imagelist.txt)
                    imageStore.getImageList(PLAYER_KEY),

                    Integer.parseInt(properties[PLAYER_ACTION_PERIOD]),
                    // I think this determines how fast the animation goes
                    Integer.parseInt(properties[PLAYER_ANIMATION_PERIOD]),
                    5, 5
            );
            world.tryAddEntity(entity);
        }

        return properties.length == PLAYER_NUM_PROPERTIES;
    }

    // movement strategy for player, for now it will copy the fairy's movement just to test if this works
//    public static boolean moveToPlayer(
//            Player player,
//            WorldModel world,
//            Entity target,
//            EventScheduler scheduler)
//    {
////        if (Point.adjacent(player.getPosition(), target.getPosition())) {
////            target.setHealth(target.getHealth()-1);
//////            world.removeEntity(target);
//////            scheduler.unscheduleAllEvents(target);
////            return true;
////        }
//         {
//
//            Point nextPos = player.nextPositionPlayer(world, target.getPosition());
//
//            if (!player.getPosition().equals(nextPos)) {
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(player, nextPos);
//            }
//
//            return false;
//        }
//    }

    // calculates the next pos that player takes, it will copy fairy movement just to check if scheduling actions works
    public Point nextPositionPlayer(WorldModel world, int direction)
    {
        Point newPos = this.getPosition();
        switch (direction)
        {
            case 1:
                Point pos = new Point(this.getPosition().x, this.getPosition().y-1);
                if(!world.isOccupied(pos))
                {
                    newPos = pos;
                }
                return newPos;
            case 2:
                Point pos2 = new Point(this.getPosition().x, this.getPosition().y+1);
                if(!world.isOccupied(pos2))
                {
                    newPos = pos2;
                }
                return newPos;
            case 3:
                Point pos3 = new Point(this.getPosition().x-1, this.getPosition().y);
                if(!world.isOccupied(pos3))
                {
                    newPos = pos3;
                }
                return newPos;
            case 4:
                Point pos4 = new Point(this.getPosition().x+1, this.getPosition().y);
                if(!world.isOccupied(pos4))
                {
                    newPos = pos4;
                }
                return newPos;
        }
        return newPos;
    }

    // this activity mimics the fairy activity just to see if scheduling actions works, we will change this later to whatever
    // activity we want it to be
    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point north = new Point(this.getPosition().x, this.getPosition().y-1);
        Point south = new Point(this.getPosition().x, this.getPosition().y+1);
        Point left = new Point(this.getPosition().x-1, this.getPosition().y);
        Point right = new Point(this.getPosition().x+1, this.getPosition().y);
        if (!world.isOccupied(north))
        {
            TREE tree = new TREE("tree_" + this.getId(), north,
                    imageStore.getImageList(TREE.TREE_KEY), TREE.getTreeActionPeriod(), TREE.getTreeAnimationPeriod(), TREE.getTreeHealth(), 0);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);
        }
        else if (!world.isOccupied(south))
        {
            TREE tree = new TREE("tree_" + this.getId(), south,
                    imageStore.getImageList(TREE.TREE_KEY), TREE.getTreeActionPeriod(), TREE.getTreeAnimationPeriod(), TREE.getTreeHealth(),0);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);
        }
        else if (!world.isOccupied(left))
        {
            TREE tree = new TREE("tree_" + this.getId(), left,
                    imageStore.getImageList(TREE.TREE_KEY), TREE.getTreeActionPeriod(), TREE.getTreeAnimationPeriod(), TREE.getTreeHealth(),0);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);
        }
        else if (!world.isOccupied(right))
        {
            TREE tree = new TREE("tree_" + this.getId(), right,
                    imageStore.getImageList(TREE.TREE_KEY), TREE.getTreeActionPeriod(), TREE.getTreeAnimationPeriod(), TREE.getTreeHealth(),0);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);
        }
//        scheduler.scheduleEvent(this,
//                new Activity(this, world, imageStore),
//                this.getActionPeriod());
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
