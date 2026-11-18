@echo off
chcp 65001 > nul
echo ==============================================
echo   Compilando Snake Game em Java...
echo ==============================================

if not exist bin mkdir bin

javac -encoding UTF-8 -d bin src/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [SUCESSO] Compilação concluída com sucesso!
    echo Os arquivos compilados estão na pasta 'bin/'.
) else (
    echo.
    echo [ERRO] Ocorreu uma falha durante a compilação.
)
pause
