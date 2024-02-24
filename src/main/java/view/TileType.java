package view;

public enum TileType {
    WHEAT("src/main/resources/wheat.png", 1),
    ORE("src/main/resources/ore.png", 2),
    WOOD("src/main/resources/wood.png", 3),
    CLAY("src/main/resources/clay.png", 4),
    WOOL("src/main/resources/wool.png", 5),
    DESERT("src/main/resources/desert.png", 0);

    private String imagePath;
    private int id;

    TileType(String s, int i) {
        this.imagePath = s;
        this.id = i;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id;
    }
}
