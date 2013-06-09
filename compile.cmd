@echo off
REM This script will compile Battle for Atlantis using the bundled apache-ant and jdk
call tools\setbits.cmd
tools\jdk-7u21-windows\bin%bits%\java -cp tools\apache-ant-1.9.1\lib\*;tools\jdk-7u21-windows\lib\* -Dant.home=tools\apache-ant-1.9.1 org.apache.tools.ant.Main -f build.xml
echo.
pause