package common.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import common.model.dto.instruction.Instruction;

public class InstructionEncoder implements Encoder.Text<Instruction> {

    private static Gson gson = new Gson();

    @Override
    public String encode(Instruction instruction) throws EncodeException {
        return gson.toJson(instruction);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
