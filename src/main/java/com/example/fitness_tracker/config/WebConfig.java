package com.example.fitness_tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // the path to the folder where pics are stored
        Path uploadDir = Paths.get("uploads/profile-pics");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        // any request to /uploads/profile-pics/** will be mapped to the file system location
        registry.addResourceHandler("/uploads/profile-pics/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}