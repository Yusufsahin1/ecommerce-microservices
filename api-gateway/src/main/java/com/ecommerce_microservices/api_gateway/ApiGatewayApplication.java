package com.ecommerce_microservices.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> userServiceRoute() {
		return route("user-service")
				.route(path("/api/v1/users/**"), http())
				.filter(lb("USER-SERVICE"))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> productServiceRoute() {
		return route("product-service")
				.route(path("/api/v1/products/**"), http())
				.filter(lb("PRODUCT-SERVICE"))
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> orderServiceRoute() {
		return route("order-service")
				.route(path("/api/v1/orders/**"), http())
				.filter(lb("ORDER-SERVICE"))
				.build();
	}

}
