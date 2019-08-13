<%@page import="Ljc.JFramework.Utility.RSACryptoServiceProvider"%>
<%@page import="Ljc.JFramework.Utility.AesEncryHelper"%>
<%@page import="Core.MongoTestCore"%>
<%@page import="Ljc.JFramework.SOA.ESBClient"%>
<%@page import="Ljc.JFramework.SOA.SOARedirectRequest"%>
<%@page import="Ljc.JFramework.SOA.SOARequest"%>
<%@page import="Ljc.JFramework.SOA.ESBConfig"%>
<%@page import="Ljc.JFramework.Utility.EventHandler"%>
<%@page import="Ljc.JFramework.Utility.ProcessTraceUtil"%>
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
//TestService.LinixService.main(new String[]{});
//TestService.Service1.StartService(null);
PrintWriter pw=response.getWriter();

//RSACryptoServiceProvider rsa=new RSACryptoServiceProvider();
//pw.print("公钥："+rsa.getPublicKeyStr()+"<br/>");
//pw.print("私钥："+rsa.getPrivateKeyStr()+"<br/>");
//pw.print("公钥："+rsa.ToXmlString(false)+"<br/>");
//pw.print("私钥："+rsa.ToXmlString(true)+"<br/>");

//pw.print(RSACryptoServiceProvider.encryptBASE64(RSACryptoServiceProvider.decodePrivateKeyFromXml("<RSAKeyValue><Modulus>koen1gO6MgfOJmjKKMEMxlgmIjTxx6Sk8wHMFeK6VAxiw2xsO+bx+maXsRlMOkAvE18TqQHpVm4464cb77BjPiW6w0V1Ds+JctdnUHkKQOUIpO7SC0zVQPPr0xGp+5ezsfx4/idopreZU9BYaRx6yfbDkMozaQNzUV0y1YyVrjU=</Modulus><Exponent>AQAB</Exponent><P>zilSrf97ffXMEM2K31U3F+yzpykY5hV8JV4zbzBEmcPeYUN03t+t848FTQqIeSE3VnEuHxir7nsacTwZosl+Aw==</P><Q>tfPtXXK1HIrlr3wc4D1hZEc/4f+MgxfTdJpozOtXp4U00xdKqA0XAwuWyIshSrRXRUHtKqKWcjhRan1zaxypZw==</Q><DP>gKgzK9kLdUEmt7m+F7/zYN/cAc7Y9gWkcyGFkbcaqKceqa5SSiBnS18O3NRc9NLw2OZK9ScNbQewKchq6zc5hw==</DP><DQ>cptyp6nY6mGSyzYh+hyve7plQrT0jJ41UvzvhiO20o3U+CFzpOaZ2BE0qJz6G1P8pGMiP/ipSsiuf6UFTjJ/gQ==</DQ><InverseQ>oDUGPYlDG4EKl+JKvTU3YzlXnh2GsGzjuRyCN72TNpPynXB3SNWAchIHOYUd8gkAESCYdSaIM9PYMSUEk7qtVw==</InverseQ><D>d48ckPFloaDgwlJKcUpjhAs0wkB07zMWK/nRbiIbaqzYgSAciBv+YRQIvcYofncUcjfnsMUQgSdaZkNNths0PaqY/RP+dxTK2dJkruD2E4HF2iFbQPmqdLGyU5GYo429XWVn2C3WJvP0YjGO6dYIrA+3tEhowQ2WiktEjLRhwBk=</D></RSAKeyValue>").getEncoded()));

//for(java.net.InetAddress addr: Ljc.JFramework.Utility.NetWorkUtil.getIpV4Address())
//{
//	pw.write(addr.getHostAddress()+"<br>");
//}

//MsqylHelper.TestConn();

//java.util.List<Entity.BlogType> list=new MongoTestCore().GetBlogTypes();
//pw.write(String.valueOf(list.size()));

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

   
  //String helloMsg= ESBClient.DoSOARequest(String.class, 100, 1, "hello,李金川");
  //pw.print("hellosg:"+helloMsg);
  
  ESBConfig cfg= ESBConfig.ReadConfig();
  
  //Core.PersionService service=new Core.PersionService();
  //service.Login(null, null);
  
  //Thread.sleep(60000);

  
  pw.print("<br/>");
  pw.print(cfg.getESBServer());
  pw.print("<br/>");
  pw.print(cfg.getESBPort());
  pw.print("<br/>");
  pw.print(cfg.getAutoStart());
  pw.print("<br/>");
  pw.print(cfg.getSecurity());
  pw.print("<br/>");
  
  String respstr=ESBClient.DoSOARequest(String.class, 0, 1, "hello");
  pw.print("esbecho:"+respstr);
  
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