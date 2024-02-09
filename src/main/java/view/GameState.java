package view;


public enum GameState {
    Board;

    private static GameState state = Board;

    public static GameState getState() {
        return state;
    }

    public static void setState(GameState state) {
        GameState.state = state;
    }

}
