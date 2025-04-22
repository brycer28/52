package logic;

import java.io.Serializable;

/**
 * GAME MESSAGE SPECIFICATIONS
 * 
 * Each message should have a MessageType and corresponding Data
 *
 * LOGIN: new GameMessage(mt=LOGIN, data=LoginData)
 *  - Send server a login credential request
 *
 * LOGIN_SUCCESS: new GameMessage(mt=LOGIN_SUCCESS, data=LoginData)
 *  - Client->Server sends a LoginData
 *  - In handling, Server sends a LOGIN_SUCCESS with a new User object
 *
 * CREATE_ACC: new GameMessage(mt=CREATE_ACC, data=CreateAccData)
 *   - Send server a create account request
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
        LOGIN, LOGIN_SUCCESS, CREATE_ACC, START_GAME, NOTIFY_TURN, PLAYER_ACTION, STATE_UPDATE, WINNER, ERROR
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

    public static class RaiseAction implements Serializable {
        private TexasHoldem.Options option;
        private int raiseAmt;

        public RaiseAction(TexasHoldem.Options option, int raiseAmt) {
            this.option = option;
            this.raiseAmt = raiseAmt;
        }
        public TexasHoldem.Options getOption() { return option; }
        public int getRaiseAmt() { return raiseAmt; }
    }
}
