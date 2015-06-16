rm -rf build

mkdir build

cp serviceLin/* build/

mvn clean

mvn package

cp target/GtfsrtExporter-0.0.1-SNAPSHOT.jar build/GtfsrtExporter.jar

cp src/main/resources/usage.txt build/

