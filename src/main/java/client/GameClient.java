package client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@SpringBootApplication
@ComponentScan({"client","common"})
public class GameClient {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GameClient.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
