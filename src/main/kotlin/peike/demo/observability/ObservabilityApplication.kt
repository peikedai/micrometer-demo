package peike.demo.observability

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ObservabilityApplication

fun main(args: Array<String>) {
	runApplication<ObservabilityApplication>(*args)
}
