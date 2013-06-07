@echo off
java -cp ant-1.9.1\lib\*;jdk-1.7\lib\* -Dant.home=ant-1.9.1 org.apache.tools.ant.Main -f build.xml
echo.
pause