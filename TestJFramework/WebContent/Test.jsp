<%@page import="Ljc.JFramework.SOA.ESBClient"%>
<%@page import="Ljc.JFramework.SOA.SOARedirectRequest"%>
<%@page import="Ljc.JFramework.SOA.SOARequest"%>
<%@page import="Ljc.JFramework.SOA.ESBConfig"%>
<%@page import="Ljc.JFramework.Utility.EventHandler"%>
<%@page import="Ljc.JFramework.Utility.ProcessTraceUtil"%>
<%@page import="java.lang.reflect.Parameter"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.ParameterizedType"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Date"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Ljc.JFramework.TypeUtil.DateTime"%>
<%@page import="Ljc.JFramework.TypeUtil.UByte"%>
<%@page import="Ljc.JFramework.TypeUtil.UInt64"%>
<%@page import="Ljc.JFramework.TypeUtil.UShort"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.LinkedList"%>
<%@page import="Ljc.JFramework.Utility.BitConverter"%>
<%@page import="Ljc.JFramework.Utility.StringUtil"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="Ljc.JFramework.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
PrintWriter pw=response.getWriter();
//Core.CallBackTest ct=new Core.CallBackTest();
String path=this.getClass().getClassLoader().getResource("").getPath();
System.out.println(path);
java.io.InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");  
System.out.println("begin!!!");  
java.util.Properties properties = new java.util.Properties();  
try{  
	properties.load(inputStream);  
}catch (java.io.IOException ioE){  
    ioE.printStackTrace();  
}finally{  
    inputStream.close();  
}  
System.out.println("name:"+properties.getProperty("name"));  
  
  pw.print("<br/>");


  ESBConfig cfg= ESBConfig.ReadConfig();
  
  Core.PersionService service=new Core.PersionService();
  service.Login(null, null);
  
  //Thread.sleep(60000);

  
  pw.print("<br/>");
  pw.print(cfg.getESBServer());
  pw.print("<br/>");
  pw.print(cfg.getESBPort());
  pw.print("<br/>");
   
   //pw.write(String.valueOf(BigDecimal.valueOf(1.21).remainder(BigDecimal.valueOf(1))));
   
/*   Person p=new Person();
  p.setAge(20);
  p.setName("ljc");
  java.util.List<Integer> list=new LinkedList<Integer>();
  list.add(100);
  p.setList(list);
  Person newperson=p.serializeMe();
  
  response.getWriter().write(newperson.getAge()+" "+newperson.getName()+" "+newperson.getList().get(0)); */
%>
</body>
</html>