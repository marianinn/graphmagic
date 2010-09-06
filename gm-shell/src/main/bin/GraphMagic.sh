#!/bin/bash

CLASSPATH=

for f in lib/*.jar
do
	CLASSPATH=$CLASSPATH$f:
done

java -cp "$CLASSPATH" name.dlazerka.gm.ui.Main $@
