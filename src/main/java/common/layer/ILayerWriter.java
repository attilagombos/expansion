package common.layer;

import common.model.Board;

public interface ILayerWriter {

    String writeLayout(Board board);

    String writeColors(Board board);

    String writeForces(Board board);
}
