package server.configuration;

import static common.configuration.EndpointConfiguration.APPLICATION_BASE_URL;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private static final String APPLICATION_VIEW_NAME = APPLICATION_BASE_URL + ".html";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(APPLICATION_BASE_URL).setViewName(APPLICATION_VIEW_NAME);
    }
}
