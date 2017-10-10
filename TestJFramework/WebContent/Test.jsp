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
  
  //EntityBufCore.Serialize(new Person(), null);
  //EntityBufCore.TestSerialize((short)126);
  
/*   byte[] strbytes=BitConverter.GetBytes("中岽华人民共c和ab国");
  for(byte b :strbytes){
	  pw.write(String.valueOf(b&255)+" ");
  }
   */
   int i=0;
   while((i++)<5){
   ProcessTraceUtil.StartTrace();
   ProcessTraceUtil.Trace(String.format("%d <br/>", i));
   Person ps=new Person();
   ps.setAge(120);
   ps.setShort((short)31055);
   ps.setBigDecimal(BigDecimal.TEN);
   ps.setDate(new Date(132324321));
   ps.setName("不好600031.sz");
   HashMap<Integer,Integer> map=new HashMap<Integer,Integer>();
   map.put(123, 9987);
   map.put(12345,9985423);
   java.util.List<Integer> li=new java.util.LinkedList<Integer>();
   li.add(100);
   li.add(50);
   li.add(1997);
   li.add(199700);
   ps.setList(li);
   ps.setDic(map);
   ProcessTraceUtil.Trace("开始序列化");
   byte[] bytes= EntityBufCore.Serialize((Object)ps);
   ProcessTraceUtil.Trace("序列化完成");
   ProcessTraceUtil.Trace("开始反序列化");
   Person dcp=EntityBufCore.DeSerialize(Person.class, bytes, true);
   ProcessTraceUtil.Trace("反序列化完成");
   pw.write(dcp.getName()+"<br/>");
   for(Integer it :dcp.getList()){
	   pw.write(String.valueOf(it)+"<br/>");
   }
   pw.write("<br/>");
   
   for(byte bt:bytes){
	   pw.write(String.valueOf(bt&255)+" ");
   }
  
   pw.write(ProcessTraceUtil.PrintTrace());
   }
   
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