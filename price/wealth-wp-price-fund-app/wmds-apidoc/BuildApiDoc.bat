SET Base_Version=v4.0
SET AOC_LCUT_Version=v4.0.6
SET UK_GIC_Version=v4.0.6
SET AMH_UT_Version=v4.1.1
SET Sanbox_Path=C:\sandbox\ApiDoc_JOE\apidoc

@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

set ApiDoc_HOME=%~dp0
goto main

:Build
SET folder=%1
SET version=%2
if "%folder%"=="%Base_Version%" (copy %ApiDoc_HOME%%folder%\template\apidoc.json %ApiDoc_HOME%%folder% /y) else (call :CopyFile %folder% %version%)
call :GenApiDoc %folder%
if "%folder%"=="%Base_Version%" (del %ApiDoc_HOME%%folder%\apidoc.json /F)
goto :eof

:CopyFile
cd /d %ApiDoc_HOME%
SET folder=%1
SET version=%2
SET ParentVerionFolder=%version:~0,4%
SET SubVersion=%version:~-1%

echo ------Start to Copy Files------
echo ***Traget Folder: %ApiDoc_HOME%%folder%
copy %ApiDoc_HOME%%ParentVerionFolder%\*.* %ApiDoc_HOME%%folder% /y
for /l %%i in (1,1,%SubVersion%) do (
SET VersionFolder=%ParentVerionFolder%.%%i%
SET Source=%ApiDoc_HOME%%ParentVerionFolder%\!VersionFolder!\*.*
SET Target=%ApiDoc_HOME%%folder%\
echo ***Traget Folder: !Target!
xcopy !Source!* !Target! /s /y
)
echo ------End of Copy Files------
echo=
goto :eof

:GenApiDoc
SET folder=%1
echo ------Start to Generate Api Doc------
cd %ApiDoc_HOME%%folder%
echo ***Traget Folder: %Sanbox_Path%\%folder%
call apidoc -o %Sanbox_Path%\%folder%
echo ------End of Generate Api Doc------
echo=
goto :eof

:main

echo Please choose which API DOC want to generate:
echo 1. For All
echo 2. For AOC_LCUT
echo 3. For UK_GIC
echo 4. For %Base_Version%
echo 5. For AMH_UT
echo=

SET /p Option=

if "%Option%"=="1" (
call :Build AOC_LCUT %AOC_LCUT_Version%
call :Build UK_GIC %UK_GIC_Version%
call :Build %Base_Version%
call :Build AMH_UT %AMH_UTB_Version%
)

if "%Option%"=="2" (
call :Build AOC_LCUT %AOC_LCUT_Version%
)

if "%Option%"=="3" (
call :Build UK_GIC %UK_GIC_Version%
)

if "%Option%"=="4" (
call :Build %Base_Version%
)

if "%Option%"=="5" (
call :Build AMH_UT %AMH_UTB_Version%
)

