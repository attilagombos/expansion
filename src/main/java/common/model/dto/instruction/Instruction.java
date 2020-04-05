package common.model.dto.instruction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Instruction implements Serializable {

    private List<Step> steps = new ArrayList<>();

    public Instruction() {
    }

    public void addStep(Step newStep) {
        Step oldStep = steps.stream().filter(step -> step.getType() == newStep.getType()
                && step.getSource() == newStep.getSource()
                && step.getTarget() == newStep.getTarget())
                .findFirst().orElse(null);

        if (oldStep != null) {
            oldStep.addForces(newStep.getForces());
        } else {
            steps.add(newStep);
        }
    }

    public List<Step> getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "Instruction{" + "steps=" + steps + '}';
    }
}
