# scdf-http-message-producer
A simple producer of JSON messages. For testing and development or SCDF pipelines

## Deploying

```shell

cf push --health-check-type none -p target/http-message-producer.jar message-producer

```

## Setting The Endpoint

``` shell

cf set-env message-producer ENDPOINT https://cndescdf-dataflow-server-rhlqe0u-s1-http.cfapps.io/

```
Do not restage, running the task will pick up the Env change.

## Running The Task - Sending The Messages

```shell

cf run-task message-producer ".java-buildpack/open_jdk_jre/bin/java org.springframework.boot.loader.JarLauncher --name messages

```
