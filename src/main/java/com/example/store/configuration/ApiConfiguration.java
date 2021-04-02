package com.example.store.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfiguration {
	// --- properties 파일
	// api.base-path

	// -- yml 파일
	// api:
	// base-path:
	private String basePath;
}