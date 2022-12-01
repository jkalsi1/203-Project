import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DUDE_FULL extends Dude
{


    public DUDE_FULL(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super( id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    // don't technically need resource count ... full
//    public static DudeFull createDudeFull(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            int resourceLimit,
//            List<PImage> images) {
//        return new DudeFull("DUDE_FULL", id, position, images, resourceLimit, 0,
//                actionPeriod, animationPeriod, 0, 0);
//    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (this.getHealth() > 0)
        {
            Optional<Entity> fullTarget =
                    world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(HOUSE.class)));

            if (fullTarget.isPresent() && this.moveTo(world,
                    fullTarget.get(), scheduler)) {
                this.transformFull(world, scheduler, imageStore);
            } else {
                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        this.getActionPeriod());
            }
        }
        else
        {
            DeadDude deadDude = new DeadDude(this.getId(), this.getPosition(), imageStore.getImageList("deaddude"),0, 0);
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            world.addEntity(deadDude);
        }
    }

    private void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        DUDE_NOT_FULL miner = new DUDE_NOT_FULL( this.getId(),
                this.getPosition(),
                this.getImages(),
                this.getResourceLimit(),
                0,
                this.getActionPeriod(),
                this.getAnimationPeriod(),
                10, 10);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(VirtualWorld.getScheduler(), world, imageStore);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }
}
