package com.dynamodb.dynamodbexample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class that loads AWS credentials from a properties file
 * and updates the Spring Environment with these values.
 * Not used in test profile as tests use mock clients.
 */
@Configuration
@Profile("!test")
public class AwsCredentialsLoader {

    @Autowired
    private ConfigurableEnvironment environment;

    private static final String AWS_CREDENTIALS_FILE = "aws-credentials.properties";
    private static final String[] POSSIBLE_CREDENTIALS_PATHS = {
        "aws-credentials.properties",
        "config\\aws-credentials.properties",
        System.getProperty("user.home") + File.separator + ".aws" + File.separator + "credentials.properties"
    };

    /**
     * Loads AWS credentials from a file during bean initialization.
     * This ensures credentials are available before other beans that need them are initialized.
     */
    @PostConstruct
    public void init() {
        loadAwsCredentialsIntoEnvironment();
        System.out.println("AWS credentials loaded at application startup");
    }

    /**
     * Loads AWS credentials from a file when the application context is refreshed.
     * This serves as a backup in case credentials weren't loaded during initialization.
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadAwsCredentialsIntoEnvironment();
    }

    /**
     * Loads AWS credentials from a file and adds them to the Spring Environment.
     */
    private void loadAwsCredentialsIntoEnvironment() {
        Properties awsCredentials = loadAwsCredentials();
        if (awsCredentials != null) {
            // Add the properties to the environment with highest precedence
            environment.getPropertySources().addFirst(new PropertiesPropertySource("awsCredentials", awsCredentials));
            System.out.println("AWS credentials loaded into environment from file");
        } else {
            System.out.println("AWS credentials file not found. Using values from application.properties.");
        }
    }

    /**
     * Loads AWS credentials from a properties file.
     * Tries several possible locations for the file.
     */
    private Properties loadAwsCredentials() {
        for (String path : POSSIBLE_CREDENTIALS_PATHS) {
            try {
                File credentialsFile = new File(path);
                if (credentialsFile.exists()) {
                    Properties properties = new Properties();
                    try (FileInputStream fis = new FileInputStream(credentialsFile)) {
                        properties.load(fis);
                    }
                    System.out.println("AWS credentials loaded from: " + path);
                    return properties;
                }
            } catch (IOException e) {
                System.err.println("Error loading AWS credentials from " + path + ": " + e.getMessage());
            }
        }
        return null;
    }
}
