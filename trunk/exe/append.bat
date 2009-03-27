if not defined CLASSPATH goto emptycp
if "%SEPARATOR%" == "" goto noseparator
set CLASSPATH=%CLASSPATH%%SEPARATOR%%1
goto end

:noseparator
set CLASSPATH=%CLASSPATH% %1
goto end

:emptycp
set CLASSPATH=%1
goto end

:end