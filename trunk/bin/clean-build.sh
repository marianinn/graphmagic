#!/bin/bash
mvn clean $@
bin=`dirname $0`
$bin/build.cmd $@
