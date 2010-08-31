#!/bin/bash

CLASSPATH=

for f in lib/*.jar
do
	CLASSPATH=$CLASSPATH$f:
done

echo java -cp "$CLASSPATH" name.dlazerka.gm.ui.Main $@
echo $CLASSPATH