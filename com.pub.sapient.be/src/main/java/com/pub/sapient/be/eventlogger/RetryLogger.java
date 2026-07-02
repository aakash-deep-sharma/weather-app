package com.pub.sapient.be.eventlogger;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;

@Component
@Profile("dev")
public class RetryLogger {

	private final RetryRegistry retryRegistry;

	public RetryLogger(RetryRegistry retryRegistry) {
		this.retryRegistry = retryRegistry;
	}

	@PostConstruct
	public void registerEvents() {

		retryRegistry.retry("openWeatherRetry").getEventPublisher()
				.onRetry(event -> System.out.println("Retry attempt: " + event.getNumberOfRetryAttempts()
						+ ", last exception: " + event.getLastThrowable().getClass().getSimpleName()))
				.onSuccess(event -> System.out.println("Retry succeeded"))
				.onError(event -> System.out.println("Retry exhausted"));
	}
}