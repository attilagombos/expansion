package common.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import common.model.Instruction;

public class InstructionDecoder implements Decoder.Text<Instruction> {

    private static Gson gson = new Gson();

    @Override
    public Instruction decode(String s) throws DecodeException {
        return gson.fromJson(s, Instruction.class);
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
