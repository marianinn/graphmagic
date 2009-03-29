@echo off

:: ${pom.name}, package for scientists working in graph theory.
:: Copyright (C) 2009 Dzmitry Lazerka dlazerka@dlazerka.name
::
:: This program is free software; you can redistribute it and/or modify
:: it under the terms of the GNU General Public License version 2 as
:: published by the Free Software Foundation.
::
:: This program is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
:: GNU General Public License for more details.
::
:: You should have received a copy of the GNU General Public License along
:: with this program; if not, write to the Free Software Foundation, Inc.,
:: 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
::
:: Author: Dzmitry Lazerka dlazerka@dlazerka.name


:: ---------------------------------------------------------------------
:: Before you run ${pom.name} specify the location of the
:: JDK installation directory which will be used for running ${pom.name}
:: ---------------------------------------------------------------------
IF "%JAVA_HOME%" == "" GOTO error


:: Trick to list all the files from the "lib" directory to the %classpath% 
SET graphmagic_home=.
SET classpath=
SET separator=;
FOR %%f IN (lib\*) DO @CALL util\append %%f


:: Run
"%JAVA_HOME%\jre\bin\java" -cp "%classpath%" name.dlazerka.gm.Main %*

GOTO end
:error
ECHO ---------------------------------------------------------------------
ECHO ERROR: cannot start ${pom.name}.
ECHO No JDK found to run ${pom.name}. Please validate JAVA_HOME points to valid JDK installation.
ECHO ---------------------------------------------------------------------
PAUSE

:end
