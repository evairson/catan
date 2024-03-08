package network;

import java.io.Serializable;

public class NetworkObject implements Serializable {
    private String message;
    private int id;
    private Serializable object;
    private TypeObject type;

    public enum TypeObject {
        Message, Game, Board, ChatMessage,
    }

    public TypeObject getType() {
        return type;
    }

    public NetworkObject(TypeObject type, String message, int id, Serializable object) {
        this.type = type;
        this.message = message;
        this.id = id;
        this.object = object;
    }
    public NetworkObject(TypeObject type, String message, int id) {
        this.type = type;
        this.message = message;
        this.id = id;
        this.object = null;
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
    public void setObject(Serializable object) {
        this.object = object;
    }


}
