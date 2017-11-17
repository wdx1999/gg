<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form"  prefix="fm"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'useradd.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
      <fm:form method="post" modelAttribute="user">
         <p><fm:errors path="userCode"></fm:errors></p>
         <p>用户编码：<fm:input path="userCode"/></p>
         <p><fm:errors path="userName"></fm:errors></p>
          <p>用户名称：<fm:input path="userName"/></p>
          <p><fm:errors path="userPassword"></fm:errors></p>
           <p>用户密码：<fm:input path="userPassword"/></p>
            <p>用户性别：<fm:select path="gender">
                <fm:option value="1" selected="selected">男</fm:option>
                <fm:option value="2">女</fm:option>
            </fm:select>
            <p><fm:errors path="birthday"></fm:errors></p>
             <p>用户生日：<fm:input path="birthday" readonly="readonly" onclick="WdatePicker();"/></p>
              <p>用户电话：<fm:input path="phone"/></p>
               <p>用户地址：<fm:input path="address"/></p>
                <p>用户角色：<fm:radiobutton path="userRole" value="1"/>系统管理员
                <fm:radiobutton path="userRole" value="2"/>经理
                <fm:radiobutton path="userRole" value="3" checked="checked"/>普通用户</p>
      <input type="submit" value="提交"> 
      </fm:form>
  </body>
  <script type="text/javascript" src="${pageContext.request.contextPath }/statics/calendar/WdatePicker.js"></script>
</html>
