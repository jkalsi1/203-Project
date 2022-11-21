import processing.core.PImage;

import java.util.List;

public class STUMP extends Entity
{
    public static final String STUMP_KEY = "stump";
    public STUMP(String id, Point position, List<PImage> images, int health, int healthLimit) {
        super(id, position, images, health, healthLimit);
    }

}
