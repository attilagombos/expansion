package client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
public class GameClient {

    public static void main(String[] args) {
        SpringApplication.run(GameClient.class, args);
    }
}
