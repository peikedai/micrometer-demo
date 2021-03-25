package peike.demo.observability

import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tags
import io.micrometer.core.instrument.Timer
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.Instant
import java.util.SplittableRandom

@Component
class CronJobA(
    private val meterRegistry: MeterRegistry
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(fixedRate = 1000)
    fun run() {
        main()
    }

    private fun main() {
        val startTime = Instant.now()
        val durationTimer = meterRegistry.timer("demo.cron.duration", Tags.of("job_name", "A"))
        val itemCounter = meterRegistry.counter("demo.cron.batch.size", Tags.of("job_name", "A"))

        val rand = SplittableRandom()
        val nextDouble = rand.nextDouble()
        val sleepTime: Long = when {
            nextDouble >= 0 && nextDouble < .2 -> 250
            nextDouble >= .2 && nextDouble < .8 -> 500
            else -> 750
        }

        Mono.just("data")
            .delayElement(Duration.ofMillis(sleepTime))
            .doOnSubscribe { logger.debug("cron job A start, it will take ${sleepTime} milliseconds to complete") }
            .doOnNext { itemCounter.increment(sleepTime / 10.0) }
            .doFinally {
                logger.debug("cron job A completed")
                durationTimer.record(Duration.between(startTime, Instant.now()))
            }
            .subscribe()
    }
}
