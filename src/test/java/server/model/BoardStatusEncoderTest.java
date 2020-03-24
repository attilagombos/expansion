package server.model;

import static common.model.Color.GREEN;
import static common.model.Color.RED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.EncodeException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.encoder.BoardStatusEncoder;
import common.model.BoardStatus;
import common.model.Color;
import common.model.Forces;
import common.model.Location;

class BoardStatusEncoderTest {

    private BoardStatusEncoder underTest;

    @BeforeEach
    void setUp() {
        underTest = new BoardStatusEncoder();
    }

    @Test
    void test() throws EncodeException {
        // Given
        BoardStatus boardStatus = new BoardStatus();
        boardStatus.setColor(RED);
        boardStatus.setBase(new Location(1, 1));
        boardStatus.setDeployableForces(10);
        boardStatus.setLayout("" +
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

        Map<Color, List<Forces>> forcesByColor = new HashMap<>();
        forcesByColor.put(RED, redForces);
        forcesByColor.put(GREEN, greenForces);

        boardStatus.setForcesByColor(forcesByColor);

        // When
        String result = underTest.encode(boardStatus);

        // Then
        assertEquals("{\"color\":\"RED\",\"base\":{\"x\":1,\"y\":1},\"deployableForces\":10,"
                + "\"layout\":\""
                + "#,#,#,#,#\\r\\n"
                + "#,1, ,$,#\\r\\n"
                + "#, , , ,#\\r\\n"
                + "#,$, ,2,#\\r\\n"
                + "#,#,#,#,#\","
                + "\"forcesByColor\":{"
                + "\"GREEN\":["
                + "{\"x\":4,\"y\":4,\"count\":1},"
                + "{\"x\":4,\"y\":3,\"count\":3},"
                + "{\"x\":3,\"y\":4,\"count\":3},"
                + "{\"x\":3,\"y\":3,\"count\":3}],"
                + "\"RED\":["
                + "{\"x\":1,\"y\":1,\"count\":1},"
                + "{\"x\":2,\"y\":1,\"count\":5},"
                + "{\"x\":1,\"y\":2,\"count\":5}]}}", result);
    }
}
