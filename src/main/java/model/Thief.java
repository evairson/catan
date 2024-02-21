package model;

import java.io.Serializable;

public class Thief implements Serializable {
    // private Coordonnee cord;
    private boolean onDesert;

    Thief() {
        onDesert = true;
    }

    public void setOnDesert(boolean b) {
        onDesert = b;
    }

    public boolean isOnDesert() {
        return onDesert;
    }


}
