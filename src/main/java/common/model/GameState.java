package common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameState implements Serializable {

    private Boolean isRunning;

    private Integer rounds;

    private BoardState boardState = new BoardState();

    private List<PlayerState> playerStates;

    private Map<Color, Instruction> instructions;

    public GameState() {
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
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

    public Map<Color, Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(Map<Color, Instruction> instructions) {
        this.instructions = instructions;
    }
}
