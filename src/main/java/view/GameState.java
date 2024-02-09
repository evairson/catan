package view;


public enum GameState {
    Playing, Menu;

    private static GameState state = Playing;

    public static GameState getState() {
        return state;
    }

    public static void setState(GameState state) {
        GameState.state = state;
    }

}
