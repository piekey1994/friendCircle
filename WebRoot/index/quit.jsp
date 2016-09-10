<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%
	try
	{
		session.invalidate();
	}
	catch(Exception e){}
	response.sendRedirect("/S2013150064/login.jsp");
%>