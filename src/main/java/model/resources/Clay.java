package model.resources;

public class Clay extends Resources {

    public Clay() {
    }
    public Clay(int amount) {
        super(amount);
    }

    @Override
    public final String toString() {
        return "Clay : " + getAmount();
    }
}
