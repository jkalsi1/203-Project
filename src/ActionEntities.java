import processing.core.PImage;

import java.util.List;

public abstract class ActionEntities extends AnimatedEntity
{
    private int actionPeriod;
    public ActionEntities( String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super( id, position, images, animationPeriod, health, healthLimit);
        this.actionPeriod = actionPeriod;
    }

    // move to activity
//    public Action createActivityAction(WorldModel world, ImageStore imageStore)
//    {
//        return new Activity(this, world, imageStore, 0);
//    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
