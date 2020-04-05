package common.layer;

import common.model.game.Board;

public interface ILayerWriter {

    String writeLayout(Board board);

    String writeColors(Board board);

    String writeForces(Board board);
}
