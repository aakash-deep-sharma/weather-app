package com.pub.sapient.be.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Client;
import feign.hc5.ApacheHttp5Client;

@Configuration
public class FeignConfig {

	@Bean
	PoolingHttpClientConnectionManager connectionManager() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

		cm.setMaxTotal(5);
		cm.setDefaultMaxPerRoute(5);

		return cm;
	}

	@Bean
	CloseableHttpClient httpClient(PoolingHttpClientConnectionManager connectionManager) {

		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

	@Bean
	Client feignClient(CloseableHttpClient httpClient) {
		return new ApacheHttp5Client(httpClient);
	}
}