@echo off
powershell -Command "&{javac -Xlint:deprecation -encoding utf-8 --module-path $env:PATH_TO_FX --add-modules=javafx.controls,javafx.fxml *.java;}"
ECHO End of Compilation Process
pause