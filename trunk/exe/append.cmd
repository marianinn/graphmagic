IF NOT DEFINED classpath GOTO emptycp
IF "%separator%" == "" GOTO noseparator
SET classpath=%classpath%%separator%%1
GOTO end

:noseparator
SET classpath=%classpath% %1
GOTO end

:emptycp
SET classpath=%1
GOTO end

:end