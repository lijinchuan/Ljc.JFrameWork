JavaService.exe -install TestCoreService "%JAVA_HOME%/bin/server/jvm.dll" -Xmx128m -Djava.class.path="%JAVA_HOME%/lib/tools.jar;E:/Work/learn/Git/Ljc.JFrameWork/lib/JavaService-2.0.10/Ljc.JFramework.jar;E:/Work/learn/Git/Ljc.JFrameWork/lib/JavaService-2.0.10/Ljc.JFramework.TestCore.jar" -start TestService.Service1 -method StartService -stop com.ecservice.ECService -method StopService -out "%CD%/out.log" -err "%CD%/err.log" -current "%CD%" -auto