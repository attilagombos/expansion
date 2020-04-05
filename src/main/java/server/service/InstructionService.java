package server.service;

import static common.model.dto.instruction.StepType.DEPLOY;
import static common.model.dto.instruction.StepType.MOVE;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import common.model.dto.instruction.Instruction;
import common.model.dto.instruction.Step;
import common.model.dto.instruction.StepType;
import common.model.game.Color;

@Service
public class InstructionService {

    private final Map<Color, Instruction> instructionCache = new ConcurrentHashMap<>();

    public void updateInstruction(Color color, Instruction instruction) {
        instructionCache.put(color, instruction);
    }

    public void clearInstructions() {
        instructionCache.clear();
    }

    public List<Step> getDeployments(Color color) {
        return getStepsByType(color, DEPLOY);
    }

    public List<Step> getMovements(Color color) {
        return getStepsByType(color, MOVE);
    }

    private List<Step> getStepsByType(Color color, StepType type) {
        Instruction instruction = instructionCache.get(color);

        if (instruction != null) {
            return instruction.getSteps()
                    .stream()
                    .filter(step -> step.getType() == type)
                    .collect(toList());
        }

        return emptyList();
    }

    public boolean hasInstructions() {
        return isNotEmpty(instructionCache);
    }

    public Map<Color, Instruction> getInstructionCache() {
        return new HashMap<>(instructionCache);
    }
}
