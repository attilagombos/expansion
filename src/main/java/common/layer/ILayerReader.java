package common.layer;

import common.model.game.Board;

public interface ILayerReader {

    Board readLayout(String layoutLayer);

    void readColors(Board board, String colorsLayer);

    void readForces(Board board, String forcesLayer);
}
