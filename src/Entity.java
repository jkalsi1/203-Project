import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity
{
    //private String kind;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int health;
    private int healthLimit;



    public Entity(
            //String kind,
            String id,
            Point position,
            List<PImage> images,
            int health,
            int healthLimit)
    {
        //this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public String getId()
    {
        return id;
    }

//    public String getKind()
//    {
//        return kind;
//    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthLimit() {
        return healthLimit;
    }

    public Point getPosition()
    {
        return  position;
    }

    public int getImageIndex() {
        return imageIndex;
    }
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public List<PImage> getImages() {
        return images;
    }

    public Point setPosition(Point p)
    {
        this.position = p;

        return this.position;
    }

    public PImage getCurrentImage()
    {
        return (this).images.get(this.imageIndex);
    }


}
