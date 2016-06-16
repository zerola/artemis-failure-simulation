#!/bin/bash

OPTIONS="-Dhost=cbgc03 -Dport=5676"

java -cp "target/classes/:target/dependency/*" -Dindex=0 ${OPTIONS} com.deutscheboerse.artemisfailure.Sender &
java -cp "target/classes/:target/dependency/*" -Dindex=1 ${OPTIONS} com.deutscheboerse.artemisfailure.Sender &
java -cp "target/classes/:target/dependency/*" -Dindex=2 ${OPTIONS} com.deutscheboerse.artemisfailure.Sender &

