git pull
mvn clean compile assembly:single
sleep 10
java -jar target/no.persoft.uiatimeplan-1.0-SNAPSHOT-jar-with-dependencies-ok.jar
pause