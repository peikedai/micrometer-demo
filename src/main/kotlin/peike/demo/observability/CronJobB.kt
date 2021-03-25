package peike.demo.observability

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.util.SplittableRandom

@Component
class CronJobB(
    private val meterRegistry: MeterRegistry
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedRate = 2000)
    fun run() {
        main()
    }

    private fun main() {
        val startTime = Instant.now()
        val durationTimer = meterRegistry.timer("demo.cron.duration", Tags.of("job_name", "B"))
        val itemCounter = meterRegistry.counter("demo.cron.batch.size", Tags.of("job_name", "B"))
        val errorCounter = meterRegistry.counter("demo.cron.batch.error", Tags.of("job_name", "B"))

        Flux.interval(Duration.ofMillis(200))
            .take(5)
            .doOnNext { itemCounter.increment() }
            .flatMap {
                val rand = SplittableRandom()
                if (rand.nextDouble() > 0.5) {
                    Flux.just(it)
                } else {
                    Flux.error(Throwable("Error"))
                }
            }
            .onErrorContinue { _, u ->
                errorCounter.increment()
                Flux.just(u)
            }
            .doOnSubscribe { logger.debug("cron job B start)") }
            .doFinally {
                logger.debug("cron job B completed")
                durationTimer.record(Duration.between(startTime, Instant.now()))
            }
            .subscribe {
                logger.info("Cron job B: $it")
            }
    }
}
