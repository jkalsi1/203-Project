public class Activity extends Action
{
    private ActionEntities entity;
    public Activity(ActionEntities entity, WorldModel world, ImageStore imageStore) {
        super(world, imageStore);
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.executeActivity(this.getWorld(), this.getImageStore(), scheduler);
    }
}
