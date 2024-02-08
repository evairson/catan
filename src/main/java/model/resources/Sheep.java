package model.resources;

public class Sheep extends Resources {

    public Sheep() {
    }

    public Sheep(final int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Sheep : " + getAmount();
    }
}
