package client.strategy;

import common.model.dto.GameState;
import common.model.dto.instruction.Instruction;

public interface IStrategy {

    Instruction getInstruction(GameState gameState);
}
