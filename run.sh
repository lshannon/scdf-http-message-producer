echo "*********************************************************"
echo "   Message Task Execution                                "
echo "*********************************************************"

  #Read in the arguments to configure the Task

  echo 'Send JMS Message? ("Y" to enable JMS, default is HTTP)'
  	read USEJMS

  echo 'Send POJO Message? ("Y" to enable POJO messages, default is Simple String)'
  	read POJO

  echo 'Number Of Messages'
  	read NUMBERMESSAGES

  echo 'HTTP Endpoint'
  	read ENDPOINT

  echo 'Message'
    read MESSAGE

  # Set the Env variables

  if [ "$POJO" == "Y" ]; then
	echo "cf set-env message-producer USEJMS true"
	cf set-env message-producer USEJMS true
  fi

  if [ "$USEJMS" == "Y" ]; then
	echo "cf set-env message-producer POJO true"
	cf set-env message-producer POJO true
  else
 	echo "cf set-env message-producer ENDPOINT $ENDPOINT"
 	cf set-env message-producer ENDPOINT $ENDPOINT
  fi


  echo "cf set-env message-producer NUMBEROFMESSAGES $NUMBERMESSAGES"
  cf set-env message-producer NUMBEROFMESSAGES $NUMBERMESSAGES

  echo "cf set-env message-producer MESSAGE $MESSAGE"
  cf set-env message-producer MESSAGE $MESSAGE

  # Run the task
  echo "cf run-task message-producer ".java-buildpack/open_jdk_jre/bin/java org.springframework.boot.loader.JarLauncher" --name messages"
  cf run-task message-producer ".java-buildpack/open_jdk_jre/bin/java org.springframework.boot.loader.JarLauncher" --name messages
