package client.strategy;

import common.model.GameState;
import common.model.Instruction;

public interface Strategy {

    Instruction getInstruction(GameState gameState);
}
