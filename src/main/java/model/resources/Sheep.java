package src.main.java.model.resources;

public class Sheep extends Resources{
    public Sheep() {
    }
    public Sheep(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Sheep : " + getAmount();
    }
}
