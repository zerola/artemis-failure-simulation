#!/bin/bash

OPTIONS="-Dhost=cbgc03 -Dport=5676"

java -cp target/artemis-failure-1.0-jar-with-dependencies.jar -Dindex=0 ${OPTIONS} com.deutscheboerse.artemisfailure.Sender &
java -cp target/artemis-failure-1.0-jar-with-dependencies.jar -Dindex=1 ${OPTIONS} com.deutscheboerse.artemisfailure.Sender &
java -cp target/artemis-failure-1.0-jar-with-dependencies.jar -Dindex=2 ${OPTIONS} com.deutscheboerse.artemisfailure.Sender &

