export JAVA_HOME=/usr/local/java/jdk8

rm -rf build

mkdir -p build/resources

cp serviceRHEL/* build/

mvn clean

mvn package

cp target/GtfsrtExporter-0.0.1-SNAPSHOT.jar build/GtfsrtExpo.jar

cp src/main/resources/usage.txt build/

cp src/main/resources/config.properties build/resources/
