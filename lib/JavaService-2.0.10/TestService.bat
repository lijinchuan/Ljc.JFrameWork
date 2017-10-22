@rem echo off

@echo .

setlocal
@rem note that if JVM not found, service 'does not report an error' when startup fails, although event logged
if "%JAVA_HOME%" == "" set JAVA_HOME=d:\j2sdk1.4.2_10\jre
set JVMDIR=%JAVA_HOME%\jre\bin\server
set JSBINDIR=%CD%
set JSEXE=%JSBINDIR%\JavaServiceDebug.exe
set SSBINDIR=%JSBINDIR%


@echo . Using following version of JavaService executable:
@echo .
%JSEXE% -version
@echo .


@echo Installing service... Press Control-C to abort
@pause
@echo .

%JSEXE% -install IDSPublisher %JVMDIR%\jvm.dll -ea -start mfbb.app.Run 
@echo .
@pause

%JSEXE% -queryconfig IDS-Publisher
%JSEXE% -uninstall IDS-Publisher

@echo .
@pause
