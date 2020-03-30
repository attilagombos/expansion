package common.model;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    private BoardState boardState = new BoardState();

    private List<PlayerState> playerStates;

    public GameState() {
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(List<PlayerState> playerStates) {
        this.playerStates = playerStates;
    }
}
