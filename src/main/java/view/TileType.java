package view;

public enum TileType {
    WHEAT("src/main/resources/tiles/wheat.png", "src/main/resources/tiles/wheatS.png"),
    ORE("src/main/resources/tiles/ore.png", "src/main/resources/tiles/oreS.png"),
    WOOD("src/main/resources/tiles/wood.png", "src/main/resources/tiles/woodS.png"),
    CLAY("src/main/resources/tiles/clay.png", "src/main/resources/tiles/clayS.png"),
    WOOL("src/main/resources/tiles/wool.png", "src/main/resources/tiles/woolS.png"),
    DESERT("src/main/resources/tiles/desert.png", "src/main/resources/tiles/desertS.png");

    private String imagePath;
    private String imagePathS;

    TileType(String s, String s2) {
        this.imagePath = s;
        imagePathS = s2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImagePathS() {
        return imagePathS;
    }
}
