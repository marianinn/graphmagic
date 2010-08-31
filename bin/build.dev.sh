#!/bin/bash
bin=`dirname $0`
$bin/build.sh -Dproduction=false -DlogLevel=DEBUG $@
