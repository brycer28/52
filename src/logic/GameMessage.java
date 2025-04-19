package logic;

import java.io.Serializable;

/**
 * GAME MESSAGE SPECIFICATIONS
 * 
 * Each message should have a MessageType and corresponding Data
 * 
 * START_GAME: new GameMessage(mt=START_TURN, data=GameState)
 *  - Transmit and initial GameState object to start a game
 * 
 * NOTIFY_TURN: new GameMessage(mt=NOTIFY_TURN, data=User)
 *  - Notifies a specific user that it is their turn, await input
 * 
 * PLAYER_ACTION: new GameMessage(mt=PLAYER_ACTION, data=Option)
 *  - Transmit a players option to/from server (check, call, fold, raise)
 * 
 * STATE_UPDATE: new GameMessage(mt=STATE_UPDATE, data=GameState)
 *  - Used to broadcast stateupdate from server to all clients
 * 
 * WINNER: new GameMessage(mt=WINNER, data=User)
 *  - Used to broadcast that a winner was determined to all clients
 * 
 */

public class GameMessage<T> implements Serializable {
    private final MessageType type;
    private final T data;

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
