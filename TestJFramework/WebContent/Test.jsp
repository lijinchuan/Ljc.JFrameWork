<%@page import="java.util.LinkedList"%>
<%@page import="Ljc.JFramework.Utility.BitConverter"%>
<%@page import="Ljc.JFramework.Utility.StringUtil"%>
<%@page import="java.lang.reflect.Method"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="Ljc.JFramework.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
  Person p=new Person();
  p.setAge(20);
  p.setName("ljc");
  java.util.List<Integer> list=new LinkedList<Integer>();
  list.add(1);
  p.SetList(list);
  Person newperson=p.serializeMe();
  
  response.getWriter().write(newperson.getAge()+" "+newperson.getName()+" "+newperson.getList().get(0));
%>
</body>
</html>