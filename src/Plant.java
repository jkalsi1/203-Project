import processing.core.PImage;

import java.util.List;

public abstract class Plant extends ActionEntities
{

    public Plant( String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    public abstract boolean transformPlant(WorldModel world,
                                  EventScheduler scheduler,
                                  ImageStore imageStore);

}
