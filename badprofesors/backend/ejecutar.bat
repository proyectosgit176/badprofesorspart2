@echo off
chcp 65001 >nul
javac -encoding UTF-8 -cp "lib/sqlite-jdbc.jar" *.java
java -cp ".;lib/sqlite-jdbc.jar" Principal
pause
