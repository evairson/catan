package model.resources;

public class Ore extends Resources {
    public Ore() {
    }

    public Ore(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Ore : " + getAmount();
    }
}
