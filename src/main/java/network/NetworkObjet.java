package network;

import java.io.Serializable;

public class NetworkObjet implements Serializable {
    private String message;
    private int id;
    private Object object;

    public NetworkObjet(String message, int id, Object object) {
        this.message = message;
        this.id = id;
        this.object = object;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }


}
