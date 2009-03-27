@echo off

:: GraphMagic, package for scientists working in graph theory.
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
:: Before you run GraphMagic specify the location of the
:: JDK installation directory which will be used for running GraphMagic
:: ---------------------------------------------------------------------

IF "%JAVA_HOME%" == "" GOTO error
SET JAVA_EXE=%JAVA_HOME%\jre\bin\java.exe

SET GRAPHMAGIC_HOME=.
SET gm_shell=${pom.build.name}.jar
SET CLASSPATH=
SET SEPARATOR=;
FOR %%f IN (lib\*) DO @CALL append.bat %%f

echo %gm_shell%
rem %JAVA_EXE% -jar "%gm_shell%" %*

GOTO end
:error
echo ---------------------------------------------------------------------
echo ERROR: cannot start GraphMagic.
echo No JDK found to run GraphMagic. Please validate JAVA_HOME points to valid JDK installation.
echo ---------------------------------------------------------------------
pause
:end
