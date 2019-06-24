@echo off
ECHO Running...
powershell -Command "&{java --module-path $env:PATH_TO_FX --add-modules=javafx.controls,javafx.fxml,javafx.media game.MainGame;}"
:END

