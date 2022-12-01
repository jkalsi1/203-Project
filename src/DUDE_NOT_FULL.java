import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DUDE_NOT_FULL extends Dude
{

    public DUDE_NOT_FULL(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    // need resource count, though it always starts at 0
//    public static DudeNotFull createDudeNotFull(
//            String id,
//            Point position,
//            int actionPeriod,
//            int animationPeriod,
//            int resourceLimit,
//            List<PImage> images)
//    {
//        return new DudeNotFull("DUDE_NOT_FULL", id, position, images, resourceLimit, 0,
//                actionPeriod, animationPeriod, 0, 0);
//    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if(this.getHealth() > 0) {
            Optional<Entity> target =
                    world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(TREE.class, SAPLING.class)));

            if (!target.isPresent() || !this.moveTo(world,
                    target.get(),
                    scheduler)
                    || !this.transformNotFull(world, scheduler, imageStore)) {
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

    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            DUDE_FULL miner = new DUDE_FULL(this.getId(),
                    this.getPosition(), this.getImages(), this.getResourceLimit(), 0, this.getActionPeriod(),
                    this.getAnimationPeriod(),10, 10);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(VirtualWorld.getScheduler(), world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            this.setResourceCount(this.getResourceCount()+1);
            // only way for dude not full to regen hp (other than transforming into dude full)
            if (this.getHealth() < this.getHealthLimit())
            {
                this.setHealth(this.getHealth() + 1);
            }
            target.setHealth(target.getHealth() - 1);
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
