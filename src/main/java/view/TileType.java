package view;

public enum TileType {
    WHEAT("src/main/resources/wheat.png"),
    ORE("src/main/resources/ore.png"),
    WOOD("src/main/resources/wood.png"),
    CLAY("src/main/resources/clay.png"),
    WOOL("src/main/resources/wool.png");

    private String imagePath;

    TileType(String s) {
        this.imagePath = s;
    }

    public String getImagePath() {
        return imagePath;
    }
}





