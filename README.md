# scdf-http-message-producer
A simple producer of JSON messages. For testing and development or SCDF pipelines

## Deploying

```shell

cf push --health-check-type none -p target/http-message-producer.jar message-producer

```

## Sending Messages

```shell

cf run-task message-producer ".java-buildpack/open_jdk_jre/bin/java org.springframework.boot.loader.JarLauncher --endpoint=xxxx" --name warm-it-up

```
