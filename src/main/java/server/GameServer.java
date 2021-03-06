package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
@ComponentScan({"server","common"})
public class GameServer {

    public static void main(String[] args) {
        SpringApplication.run(GameServer.class, args);
    }
}
