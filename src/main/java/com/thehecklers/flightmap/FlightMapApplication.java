package com.thehecklers.flightmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class FlightMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightMapApplication.class, args);
	}

}

@RestController
class FMController {
	private final WebClient client = WebClient.create("http://localhost:9090");

	@GetMapping("/positions")
	Flux<Position> getPositions(@RequestParam(required = false) String oc,
								@RequestParam(required = false) String tracklo,
								@RequestParam(required = false) String trackhi) {

		var ocParam = (null == oc ? "" : "oc=" + oc);
		var trackParams = ((null == tracklo) || (null == trackhi) ? "" : "tracklo=" + tracklo +
				"&trackhi=" + trackhi);
		var allParams = ocParam +
				(trackParams.length() > 0 ? "&" + trackParams : "");

		return client.get()
				.uri("/positions" + (allParams.length() > 0 ? "?" + allParams : ""))
				.retrieve()
				.bodyToFlux(Position.class);
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Position(@JsonProperty("latitude") float lat, @JsonProperty("longitude") float lon) {}