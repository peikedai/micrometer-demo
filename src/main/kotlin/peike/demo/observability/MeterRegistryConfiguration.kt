package peike.demo.observability

import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.logging.LoggingMeterRegistry
import io.micrometer.core.instrument.logging.LoggingRegistryConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.time.Duration

@Configuration
class MeterRegistryConfiguration {
    @Bean
    @Profile("logging")
    fun prometheusRegistry(): MeterRegistry {
        return LoggingMeterRegistry(object : LoggingRegistryConfig {
            override fun get(key: String): String? {
                return null
            }

            override fun step(): Duration {
                return Duration.ofSeconds(10)
            }

            override fun logInactive(): Boolean {
                return true
            }
        }, Clock.SYSTEM)
    }
}
