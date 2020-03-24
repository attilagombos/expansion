package common.model;

import java.io.Serializable;
import java.util.List;

public class Instruction implements Serializable {

    private List<Step> steps;

    public Instruction() {
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "Instruction{" + "steps=" + steps + '}';
    }
}
