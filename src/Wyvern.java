import processing.core.PImage;

import java.util.List;

public class Wyvern extends AnimatedEntity {
    public Wyvern(String id, Point position, List<PImage> images, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, animationPeriod, health, healthLimit);
    }
}
