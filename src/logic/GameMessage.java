package logic;

import java.io.Serializable;

public class GameMessage<T> implements Serializable {
    private MessageType type;
    private T data;

    public enum MessageType {
        START_GAME, NOTIFY_TURN, PLAYER_ACTION, STATE_UPDATE, WINNER
    }

    public GameMessage(MessageType mt, T data) {
        this.type = mt;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Game Message: { type: " + type + ", data: " + data + " }";
    }
    public MessageType getType() { return type; }
    public T getData() { return data; }
}
