package common.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import common.model.dto.GameState;

public class GameStateEncoder implements Encoder.Text<GameState> {

    private static Gson gson = new Gson();

    @Override
    public String encode(GameState gameState) throws EncodeException {
        return gson.toJson(gameState);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
