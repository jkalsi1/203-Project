public class Animation extends Action
{
    private AnimatedEntity entity;
    private int repeatCount;
    public Animation(AnimatedEntity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        super( world, imageStore);
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    new Animation(this.entity, null, null, Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }
}
