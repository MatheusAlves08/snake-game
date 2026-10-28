@echo off
echo ==============================================
echo   Iniciando Snake Game...
echo ==============================================

if not exist bin\Main.class (
    echo Compilando o projeto antes da primeira execucao...
    if not exist bin mkdir bin
    javac -d bin src/*.java
)

java -cp bin Main
