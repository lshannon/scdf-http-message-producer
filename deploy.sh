echo "*********************************************************"
echo "   Message Task Set Up                                    "
echo "*********************************************************"

cf push --health-check-type none -p target/message-producer.jar message-producer