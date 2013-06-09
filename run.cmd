@echo off
REM This script will launch Battle for Atlantis. You MUST first execute the compile.cmd script !
call tools\setbits.cmd
start tools\jdk-7u21-windows\bin%bits%\javaw -cp bin;lib/* ch.hearc.p2.battleforatlantis.Main
