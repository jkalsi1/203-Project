import processing.core.PImage;

import java.util.List;

public class DeadDude extends Entity
{
    public static final String DEAD_DUDE_KEY = "deaddude";
    public DeadDude(String id, Point position, List<PImage> images, int health, int healthLimit) {
        super(id, position, images, health, healthLimit);
    }

}