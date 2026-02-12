package com.ecommerce_microservices.user_service.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import io.github.cdimascio.dotenv.DotenvBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class DotenvEnvironmentPostProcessor implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private static final String PROPERTY_SOURCE_NAME = "dotenv";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String directory = getProperty(environment, "SPRINGDOTENV_DIRECTORY", "springdotenv.directory", ".");
        String filename = getProperty(environment, "SPRINGDOTENV_FILENAME", "springdotenv.filename", ".env");
        boolean ignoreIfMissing = Boolean.parseBoolean(
                getProperty(environment, "SPRINGDOTENV_IGNORE_IF_MISSING", "springdotenv.ignoreIfMissing", "false")
        );

        Dotenv dotenv = null;
        String[] candidates = getDirectoryCandidates(directory);
        Exception lastException = null;
        for (String candidate : candidates) {
            try {
                dotenv = loadDotenv(candidate, filename, ignoreIfMissing);
                lastException = null;
                break;
            } catch (Exception e) {
                lastException = e;
            }
        }

        if (dotenv == null && lastException != null) {
            if (ignoreIfMissing) {
                return;
            }

            String workingDir = new File(".").getAbsolutePath();
            throw new IllegalStateException(
                    "Could not load .env file. Tried directories '" + String.join("', '", candidates) +
                            "' with filename '" + filename + "' (working directory: " + workingDir + ")",
                    lastException
            );
        }

        if (dotenv == null) {
            return;
        }

        Map<String, Object> props = new LinkedHashMap<>();
        for (DotenvEntry entry : dotenv.entries()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                props.put(entry.getKey(), entry.getValue());
            }
        }

        if (props.isEmpty()) {
            return;
        }

        if (environment.getPropertySources().contains(PROPERTY_SOURCE_NAME)) {
            environment.getPropertySources().remove(PROPERTY_SOURCE_NAME);
        }

        MapPropertySource propertySource = new MapPropertySource(PROPERTY_SOURCE_NAME, props);
        environment.getPropertySources().addLast(propertySource);
    }

    private Dotenv loadDotenv(String directory, String filename, boolean ignoreIfMissing) {
        DotenvBuilder builder = Dotenv.configure()
                .directory(directory)
                .filename(filename);

        if (ignoreIfMissing) {
            builder.ignoreIfMissing();
        }

        return builder.load();
    }

    private String[] getDirectoryCandidates(String configuredDirectory) {
        if (configuredDirectory != null && !configuredDirectory.isBlank() && !configuredDirectory.equals(".")) {
            return new String[]{configuredDirectory};
        }

        return new String[]{
                "user-service",
                ".",
                "..",
                "../user-service"
        };
    }

    private String getProperty(ConfigurableEnvironment environment, String envKey, String springKey, String defaultValue) {
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }

        String fromSysProps = System.getProperty(envKey);
        if (fromSysProps != null && !fromSysProps.isBlank()) {
            return fromSysProps;
        }

        String fromSpring = environment.getProperty(springKey);
        if (fromSpring != null && !fromSpring.isBlank()) {
            return fromSpring;
        }

        return defaultValue;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
