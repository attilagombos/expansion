package common.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import common.model.BoardStatus;

public class BoardStatusDecoder implements Decoder.Text<BoardStatus> {

    private static Gson gson = new Gson();

    @Override
    public BoardStatus decode(String s) throws DecodeException {
        return gson.fromJson(s, BoardStatus.class);
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
