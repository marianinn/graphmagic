call mvn clean %*
call build.cmd -Dproduction=false -DlogLevel=DEBUG %*
