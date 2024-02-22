package model.resources;

public class Wool extends Resources {

    public Wool() {
    }

    public Wool(final int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Wool : " + getAmount();
    }
}
