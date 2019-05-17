@echo off
findstr /m /c:"public static void main" *.java
IF ERRORLEVEL 1 GOTO FAILED

:OK
for /f %%i in ('findstr /m /c:"public static void main" *.java') do set RESULT= %%i
cls
echo Main found in %RESULT%
for /f "delims=." %%a in ("%RESULT%") do set FILENAME= %%a
::echo %FILENAME%
::pause

ECHO Running...
powershell -Command "&{java --module-path $env:PATH_TO_FX --add-modules=javafx.controls,javafx.fxml %FILENAME%;}"
GOTO END
::pause

:FAILED
cls
ECHO ERROR: Main not found in current directory.
pause

:END

