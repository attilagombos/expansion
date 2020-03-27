package common.model;

import java.io.Serializable;

public class GameState implements Serializable {

    private PlayerState playerState;

    private BoardState boardState;

    public GameState() {
    }

    public GameState(PlayerState playerState, BoardState boardState) {
        this.playerState = playerState;
        this.boardState = boardState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }
}
