package co.istad.iteecomerc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class ResourceHandlersConfig implements WebMvcConfigurer {

    @Value("${file.storage-location}")
    private String storageLocation;

    @Value("${file.client-path}")
    private String clientPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convert system absolute path safely to a proper file URI string
        String resourceLocation = Paths.get(storageLocation).toUri().toString();

        registry.addResourceHandler(clientPath + "/**")
                .addResourceLocations(resourceLocation);
    }
}