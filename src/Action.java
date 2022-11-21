import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * An action that can be taken by an entity
 */
public abstract class Action
{
    private WorldModel world;
    private ImageStore imageStore;


    public Action(
            WorldModel world,
            ImageStore imageStore)
    {
        this.world = world;
        this.imageStore = imageStore;

    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    public WorldModel getWorld() {
        return world;
    }

    public abstract void executeAction(EventScheduler scheduler);

}
