package common.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import common.model.dto.GameState;

public class GameStateDecoder implements Decoder.Text<GameState> {

    private static Gson gson = new Gson();

    @Override
    public GameState decode(String s) throws DecodeException {
        return gson.fromJson(s, GameState.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
