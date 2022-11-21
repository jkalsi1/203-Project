import processing.core.PImage;

import java.util.List;

public class HOUSE extends Entity {

    public static final String HOUSE_KEY = "house";
    private static final int HOUSE_NUM_PROPERTIES = 4;
    private static final int HOUSE_ID = 1;
    private static final int HOUSE_COL = 2;
    private static final int HOUSE_ROW = 3;

    public HOUSE(String id, Point position, List<PImage> images, int health, int healthLimit) {
        super( id, position, images, health, healthLimit);
    }

//    public static House createHouse(
//            String id, Point position, List<PImage> images) {
//        return new House("HOUSE", id, position, images,
//                0, 0);
//    }

    public static boolean parseHouse(
            String[] properties, WorldModel world, ImageStore imageStore) {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            Entity entity = new HOUSE(properties[HOUSE_ID], pt,
                    imageStore.getImageList(
                            HOUSE_KEY), 0, 0);
            world.tryAddEntity(entity);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }
}
