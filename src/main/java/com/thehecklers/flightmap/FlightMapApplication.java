package com.thehecklers.flightmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@SpringBootApplication
public class FlightMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightMapApplication.class, args);
	}

}

@Controller
class FMController {
	private final WebClient client = WebClient.create("http://localhost:9090/positions");

	@GetMapping("/positions")
	@ResponseBody
	Iterable<Position> getPositions() {
//		Test data
//		return Arrays.asList(new Position(38.6240528F, -90.1771256F), new Position(38.5647893F,-90.165382F));

		return client.get()
				.retrieve()
				.bodyToFlux(Position.class)
				.collectList()
				.block();
	}

	@GetMapping("/map")
	String loadMap(Model model) {
		return "mapdisplay";
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
record Position(@JsonProperty("latitude") float lat, @JsonProperty("longitude") float lon) {}