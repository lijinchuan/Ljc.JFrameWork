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
  PrintWriter pw=response.getWriter();

Map<String,String> hm=new HashMap<String,String>();
hm.put("s", "c");

if (ParameterizedType.class.isAssignableFrom(hm.getClass().getGenericSuperclass().getClass())) {  
    System.out.print("getActualTypeArguments:");  
    for (java.lang.reflect.Type t1 : ((ParameterizedType) hm.getClass().getGenericSuperclass()).getActualTypeArguments()) {  
        System.out.print(t1+ ",");  
    }  
    System.out.println();  
    
    
}  

Method m = Person.class.getMethod("applyHashMap", HashMap.class);
java.lang.reflect.Type[] type = m.getGenericParameterTypes();
for(int i=0; i<type.length; i++) {
    ParameterizedType pt =  (ParameterizedType)type[i];
    java.lang.reflect.Type[] temp = pt.getActualTypeArguments();
    for(int j=0; j<temp.length; j++) {
        System.out.println(temp[j]);
    }
}
  
  //EntityBufCore.Serialize(new Person(), null);
  //EntityBufCore.TestSerialize((short)126);
  
/*   byte[] strbytes=BitConverter.GetBytes("中岽华人民共c和ab国");
  for(byte b :strbytes){
	  pw.write(String.valueOf(b&255)+" ");
  }
   */
  
   Person ps=new Person();
   ps.setAge(120);
   ps.setBigDecimal(BigDecimal.TEN);
   ps.setDate(new Date(132324321));
   ps.setName("中化人人人从一个孙");
   
   byte[] bytes= EntityBufCore.Serialize((Object)ps);
   
   for(byte bt:bytes){
	   pw.write(String.valueOf(bt&255)+" ");
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