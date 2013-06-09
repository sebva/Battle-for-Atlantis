@echo off
REM This script will create two .jar files in the current directory : bfa-src.jar and bfa-res.jar
call tools\setbits.cmd
call tools\jdk-7u21-windows\bin%bits%\jar cvf bfa-src.jar -C bin ch
call tools\jdk-7u21-windows\bin%bits%\jar cvf bfa-res.jar -C res .
echo.
pause