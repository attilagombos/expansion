package common.configuration;

public class EndpointConfiguration {

    public static final String APPLICATION_BASE_URL = "/expansion";

    public static final String GAME_ENDPOINT_SERVER_PATH = APPLICATION_BASE_URL + "/game/{player}";

    public static final String GAME_ENDPOINT_CLIENT_PATH = APPLICATION_BASE_URL + "/game/";

    public static final String WEB_ENDPOINT_PATH = APPLICATION_BASE_URL + "/web";
}
