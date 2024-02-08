package model.resources;

public class Wheat extends Resources {

    public Wheat() {
    }

    public Wheat(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Wheat : " + getAmount();
    }
}
