package common.encoder;

import static common.model.Color.GREEN;
import static common.model.Color.RED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.websocket.EncodeException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.model.BoardState;
import common.model.Color;
import common.model.Forces;
import common.model.GameState;
import common.model.Location;
import common.model.PlayerState;

class GameStateEncoderTest {

    private GameStateEncoder underTest;

    @BeforeEach
    void setUp() {
        underTest = new GameStateEncoder();
    }

    @Test
    void test() throws EncodeException {
        // Given
        PlayerState playerState = new PlayerState(RED, new Location(1, 1), 10);

        BoardState boardState = new BoardState();

        boardState.setLayout("" +
                "#,#,#,#,#\r\n" +
                "#,1, ,$,#\r\n" +
                "#, , , ,#\r\n" +
                "#,$, ,2,#\r\n" +
                "#,#,#,#,#");

        List<Forces> redForces = new ArrayList<>();
        redForces.add(new Forces(new Location(1, 1), 1));
        redForces.add(new Forces(new Location(2, 1), 5));
        redForces.add(new Forces(new Location(1, 2), 5));

        List<Forces> greenForces = new ArrayList<>();
        greenForces.add(new Forces(new Location(4, 4), 1));
        greenForces.add(new Forces(new Location(4, 3), 3));
        greenForces.add(new Forces(new Location(3, 4), 3));
        greenForces.add(new Forces(new Location(3, 3), 3));

        Map<Color, List<Forces>> forcesByColor = new TreeMap<>();
        forcesByColor.put(RED, redForces);
        forcesByColor.put(GREEN, greenForces);

        boardState.setForces(forcesByColor);

        GameState gameState = new GameState(playerState, boardState);

        // When
        String result = underTest.encode(gameState);

        // Then
        assertEquals("{" +
                "\"playerState\":{" +
                "\"color\":\"RED\"," +
                "\"base\":{\"x\":1,\"y\":1}," +
                "\"reinforcements\":10}," +
                "\"boardState\":{" +
                "\"layout\":\"" +
                "#,#,#,#,#\\r\\n" +
                "#,1, ,$,#\\r\\n" +
                "#, , , ,#\\r\\n" +
                "#,$, ,2,#\\r\\n" +
                "#,#,#,#,#\"," +
                "\"forces\":{" +
                "\"RED\":[" +
                "{\"x\":1,\"y\":1,\"count\":1}," +
                "{\"x\":2,\"y\":1,\"count\":5}," +
                "{\"x\":1,\"y\":2,\"count\":5}]," +
                "\"GREEN\":[" +
                "{\"x\":4,\"y\":4,\"count\":1}," +
                "{\"x\":4,\"y\":3,\"count\":3}," +
                "{\"x\":3,\"y\":4,\"count\":3}," +
                "{\"x\":3,\"y\":3,\"count\":3}]}}}", result);
    }
}
