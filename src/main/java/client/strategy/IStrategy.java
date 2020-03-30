package client.strategy;

import common.model.GameState;
import common.model.Instruction;

public interface IStrategy {

    Instruction getInstruction(GameState gameState);
}
