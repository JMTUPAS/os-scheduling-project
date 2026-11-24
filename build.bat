@echo off
rem build.bat - compiles the project and makes OSProject.jar
rem --release 8 so the jar also runs on lab computers with an older Java
rem Written by: Ivan Allen Torres (Leader)

if exist out rmdir /s /q out
mkdir out

javac --release 8 -Xlint:-options -d out src\*.java
if errorlevel 1 goto failed

rem the jar tool is sometimes not in PATH, so look for it in the JDK folder
set JARTOOL=jar
where jar >nul 2>nul
if errorlevel 1 for /d %%d in ("C:\Program Files\Java\jdk*") do set JARTOOL="%%d\bin\jar.exe"

%JARTOOL% cfe OSProject.jar Main -C out .
if errorlevel 1 goto failed
echo.
echo Done. Double-click OSProject.jar or run: java -jar OSProject.jar
goto end

:failed
echo.
echo Build failed, check the errors above.

:end
