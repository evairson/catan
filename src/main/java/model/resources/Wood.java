package src.main.java.model.resources;

public class Wood extends Resources{
    public Wood() {
    }
    public Wood(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Wood : " + getAmount();
    }
}
