# SCDF Message Producer

This is a task that can be deploy to PCF that produces messages for testing and development of SCDF pipelines

It can post messages on a JMS (to be consumed by SCDF) or post messages to an HTTP Source.

## Compiling The Application

```shell

mvn -DskipTests=true clean package

```

## Deploying

```shell

cf push --health-check-type none --no-start -p target/message-producer.jar message-producer

```

## Configuring Message Behavior

### Number Of Messages

This for both types of messages.

``` shell

cf set-env message-producer NUMBEROFMESSAGES 10

```

### POJO message

This will send a 'SimpleMessage' Pojo. It contains a Id and Name attribute. The default (if this is not set) is a string.

``` shell

cf set-env message-producer POJO true

```

Do not restage, running the task will pick up the Env change.


## Configuring HTTP Messages

``` shell

cf set-env message-producer ENDPOINT https://cndescdf-dataflow-server-rhlqe0u-s1-http.cfapps.io/

```

## Configuring JMS Messages

Using these settings will publish messages to a JMS Queue. Currently this is hard coded in the application (application.properties). At some point it will be made into an arguement.

``` shell

cf set-env message-producer USEJMS true

```

## Sending The Messages

```shell

cf run-task message-producer ".java-buildpack/open_jdk_jre/bin/java org.springframework.boot.loader.JarLauncher" --name messages

```
