package common.model;

import java.io.Serializable;

public class Step implements Serializable {

    private StepType type;

    private Location source;

    private Location target;

    private Integer forces;

    public Step() {
    }

    public Step(StepType type, Location source, Location target, Integer forces) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.forces = forces;
    }

    public StepType getType() {
        return type;
    }

    public Location getSource() {
        return source;
    }

    public Location getTarget() {
        return target;
    }

    public Integer getForces() {
        return forces;
    }

    @Override
    public String toString() {
        return "Step{" + "type=" + type + ", source=" + source + ", target=" + target + ", forces=" + forces + '}';
    }
}
