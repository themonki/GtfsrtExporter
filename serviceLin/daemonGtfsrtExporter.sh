#!/bin/bash
# -*- mode: sh -*-
# vi: set ft=sh :


##
# construye los comandos necesarios para ejecucion del demonio:
# si se pasa como parametro java devuelve el jdk a utilizar, si esta instanciado
# en orden JAVA_8_HOME, JAVA_HOME si no estan instanciadas entonces se toma /usr/local/java/jdk.
# si se pasa como parametro args devuelve los argumentos necesarios para ejecutar.
# si se pasa comand devuelve el comando usual
##

#cambiar de acuerdo al Makefile
COMMON_NAME=GtfsrtExporter
DESC="Exportador creador de URL para Gtfs-realtime"

#Si se usa symbol link
WORK_PATH="/usr/local/$COMMON_NAME"
cd "$WORK_PATH"

CONFIG_PATH=/usr/local/etc/$COMMON_NAME/resources
FILE_JAR="$COMMON_NAME.jar"
JVM_ARGS="-Xms50m -Xmx100m"
LOG_ARG=""
FILE_ARGS="--vehiclePositionsPathRead=/home/monki/metrocali/GTFS/gtfsrt/vehiclePositions.pb --tripUpdatesPathRead=/home/monki/metrocali/GTFS/gtfsrt/tripUpdates.pb --alertsPathRead=/home/monki/metrocali/GTFS/gtfsrt/alerts.pb"
URL_ARGS="--alertsUrl=http://localhost:8082/alerts --tripUpdatesUrl=http://localhost:8082/trip-updates --vehiclePositionsUrl=http://localhost:8082/vehicle-positions"

if [ ! -z "$JAVA_8_HOME" ]; then
	JAVA_HOME="$JAVA_8_HOME"
else 
	if [ -z "$JAVA_HOME" ]; then
	JAVA_HOME="/usr/local/java/jdk8"
	fi
fi

JAVA_BIN="$JAVA_HOME/bin/java"

ARGS="$JVM_ARGS $LOG_ARG -jar $FILE_JAR $FILE_ARGS $URL_ARGS"

COMAND="$JAVA_BIN $ARGS"

if [ "$1" = "java" ]; then
	echo $JAVA_BIN
fi

if [ "$1" = "args" ]; then
	echo $ARGS
fi 

if [ "$1" = "comand" ]; then
	echo $COMAND
fi

if [ "$1" = "desc" ]; then
	echo $DESC
fi

function handle_sigint()
	{	
		echo -n "Kill subproces PID $!"
	    kill -1 $!
	}

##Ejecutar el comando
##Capturar señales de interrupcion
if [ "$1" = "exec" ]; then
	trap handle_sigint SIGHUP SIGINT SIGTERM SIGKILL
	$COMAND &
	wait
fi

