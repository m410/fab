@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Brzy Fab(ricate) Start Up Batch script
@REM ----------------------------------------------------------------------------

@echo off
setLocal EnableDelayedExpansion

set LAUNCHER=org.m410.fabricate.Main
set JAVA_OPTS="-Xmx512m"

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkMHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:chkMHome
for %%i in ("%~dp0..") do set "FAB_HOME=%%~fi"
goto checkMBat

echo.
echo ERROR: FAB_HOME not found in your environment.
echo Please set the FAB_HOME variable in your environment to match the
echo location of the Maven installation
echo.
goto error



:checkMBat
if exist "%FAB_HOME%\bin\fab.bat" goto init

echo.
echo ERROR: FAB_HOME is set to an invalid directory.
echo FAB_HOME = "%FAB_HOME%"
echo Please set the FAB_HOME variable in your environment to match the
echo location of the Brzy installation
echo.
goto error
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Windows NT with Novell Login
if "%OS%"=="WINNT" goto WinNTNovell

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

:WinNTNovell

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET JAVA_EXE="%JAVA_HOME%\bin\java.exe"


@REM create the classpath

set FABPATH="%FAB_HOME%"
set CLASSPATH="
for /R %FABPATH%\lib %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"

@REM set CLASSPATH=%FAB_HOME%\lib\commons-beanutils-1.9.1.jar;%FAB_HOME%\lib\commons-cli-1.3.1.jar;%FAB_HOME%\lib\commons-codec-1.9.jar;%FAB_HOME%\lib\commons-collections-3.2.1.jar;%FAB_HOME%\lib\commons-configuration2-2.1.jar;%FAB_HOME%\lib\commons-lang3-3.3.2.jar;%FAB_HOME%\lib\commons-logging-1.1.1.jar;%FAB_HOME%\lib\fab-cli-0.2.jar;%FAB_HOME%\lib\fluent-hc-4.5.2.jar;%FAB_HOME%\lib\httpclient-4.5.2.jar;%FAB_HOME%\lib\httpcore-4.4.4.jar;%FAB_HOME%\lib\org.apache.felix.framework-4.4.0.jar;%FAB_HOME%\lib\org.apache.felix.main-4.4.0.jar;%FAB_HOME%\lib\snakeyaml-1.13.jar;%FAB_HOME%\lib\yaml-configuration-0.2.jar;


goto runfab


@REM Start FAB
:runfab

@REM echo JAVA_EXE = %JAVA_EXE%
@REM echo FAB_HOME = %FAB_HOME%
@REM echo JAVA_OPTS = %JAVA_OPTS%
@REM echo CLASSPATH = %CLASSPATH%
@REM echo LAUNCHER = %LAUNCHER%
@REM echo CMD_LINE_ARGS = %CMD_LINE_ARGS%

@REM %JAVA_EXE% %JAVA_OPTS% -classpath %CLASSPATH%  -Dfab.home="%FAB_HOME%" %LAUNCHER% %CMD_LINE_ARGS%
%JAVA_EXE% %JAVA_OPTS% -classpath %CLASSPATH%  -Dfab.home="%FAB_HOME%" %LAUNCHER% %CMD_LINE_ARGS%

if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT
if "%OS%"=="WINNT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set JAVA_EXE=
set CMD_LINE_ARGS=
goto postExec

:endNT
@endlocal & set ERROR_CODE=%ERROR_CODE%

:postExec


cmd /C exit /B %ERROR_CODE%
