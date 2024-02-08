package model.resources;

public class Stone extends Resources {
    public Stone() {
    }

    public Stone(int amount) {
        super(amount);
    }

    @Override
    public String toString() {
        return "Stone : " + getAmount();
    }
}
