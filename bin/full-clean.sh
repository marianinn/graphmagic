#!/bin/bash
mvn clean
M2_REPO=~/.m2/repository
INSTALLED_TO=name/dlazerka/gm
if [ -d $M2_REPO ] ; then 
	rm -r "$M2_REPO/$INSTALLED_TO"
fi
