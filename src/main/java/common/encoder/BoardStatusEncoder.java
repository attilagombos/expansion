package common.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import common.model.BoardStatus;

public class BoardStatusEncoder implements Encoder.Text<BoardStatus> {

    private static Gson gson = new Gson();

    @Override
    public String encode(BoardStatus boardStatus) throws EncodeException {
        return gson.toJson(boardStatus);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
