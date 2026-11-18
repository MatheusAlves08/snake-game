@echo off
chcp 65001 > nul
echo ==============================================
echo   Iniciando Snake Game...
echo ==============================================

if not exist bin\Main.class (
    echo Compilando o projeto antes da primeira execução...
    if not exist bin mkdir bin
    javac -encoding UTF-8 -d bin src/*.java
)

java -Dfile.encoding=UTF-8 -cp bin Main
