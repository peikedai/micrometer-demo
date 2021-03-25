# Micrometer Demo

This is the project for the backend group hug demo about observability. 

This project shows how to use Micrometer:

* Setup to expose Prometheus metrics
* Setup to upload to Datadog
* Use timer
* Use counter

## Expose Prometheus Metrics

First run this command to start the server

```shell
./gradlew bootRun --args='--spring.profiles.active=prometheus'
```

Then open the page http://localhost:8080/actuator/prometheus to view the metrics.

## Upload to Datadog

First, you will need to provide a valid api key in [this file](src/main/resources/application-datadog.yaml).

Then run this command to start the server

```shell
./gradlew bootRun --args='--spring.profiles.active=datadog'
```

For every 10 seconds there should be log saying:

> sending metrics batch to Datadog

and the actual request body in Micrometer sent.
