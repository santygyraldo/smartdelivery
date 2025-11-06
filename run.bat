@echo off
setlocal enabledelayedexpansion
set JAVA_HOME=C:\Program Files\Java\jdk-23
set PATH=C:\Program Files\Java\jdk-23\bin;%PATH%
call mvnw.cmd spring-boot:run
endlocal
