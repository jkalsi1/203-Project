import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends Entity
{
    private int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int animationPeriod, int health, int healthLimit) {
        super( id, position, images, health, healthLimit);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod()
    {
        return animationPeriod;
    }

    // animation of instanced entity, so it should belong in AnimatedEntity class
    public void nextImage()
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }




}
