import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point
{
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point)other).x == this.x
                && ((Point)other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public static boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y
                && Math.abs(p1.x - p2.x) == 1);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = this.distanceSquared(nearest.getPosition());

            for (Entity other : entities) {
                int otherDistance = this.distanceSquared(other.getPosition());

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public int distanceSquared(Point p1)
    {
        int deltaX = p1.x - this.x;
        int deltaY = p1.y - this.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public int distance(Point p1)
    {
        int deltaX = p1.x - this.x;
        int deltaY = p1.y - this.y;

        return Math.abs(deltaX) + Math.abs(deltaY);
    }

    public static double distanceTo(Point start, Point end) {
        double x = Math.abs(start.x-end.x);
        double y = Math.abs(start.y-end.y);
        return x+y;
    }
}
