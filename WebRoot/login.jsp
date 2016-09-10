<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.net.*"%>

<html>
	<head>
		<link rel="stylesheet" href="CSS/bootstrap.min.css">
		<style type="text/css">
	    <!--
	    	a:link {color: #66CCFF}  
			a:hover {text-decoration:underline ; cursor:pointer; color: #66CCFF} 
			a:active {color: #0000FF}  	
			body,button, input, select, textarea,h1 ,h2, h3, h4, h5, h6 { font-family: Microsoft YaHei,'宋体' , Tahoma, Helvetica, Arial, "\5b8b\4f53", sans-serif;}
	    -->
	  	</style>
	  	<script type="text/javascript" src="JS/jquery.min.js"></script>
	  	<script type="text/javascript">
			function sub()
			{
				name=document.textForm.name.value;
				password=document.textForm.password.value;
				if(name.length==0)
				{
					alert("请输入您的用户名");
					document.textForm.name.focus();
					return false;
				}
				if(password.length==0)
				{
					alert("请输入您的密码");
					document.textForm.password.focus();
					return false;
				}
				$.ajax({
					cache: false,
					type: "POST",
					url:"servlet/Login",	//把表单数据发送到servlet/Login
					data:$('#ajaxForm').serialize(),	//要发送的是ajaxFrm表单中的数据
					async: true,
					error: function(request) {
					alert("发送请求失败！");
					},
					success: function(data) {
						if(data=="true") location.href="index/index.jsp";
						else 
						{
							refresh();
							$("#loginResult").html(data);	//将返回的结果显示到loginResult中
						}
					}
					});
			}
			function refresh(){
				textForm.imgValidate.src = "validate.jsp?q="+Math.random();
			}
		</script>
	</head>
	
	<body>
		<nav class="navbar navbar-default" role="navigation">
		   <div class="navbar-header">
		      <a class="navbar-brand" href="#">朋友圈</a>
		   </div>
		   <div>
		      <ul class="nav navbar-nav ">
		         <li class="active"><a href="login.jsp">登陆</a></li>
		         <li><a href="register.jsp">注册</a></li>
		      </ul>
		   </div>
		</nav>
<%
	//获取Cookie
	String remName="",remPassword="";
	int c=0;
	Cookie[] cookies=request.getCookies();
	if(cookies!=null)
	{
		for(int i=0;i<cookies.length && c<2;i++)
		{
			if(cookies[i].getName().equals("name"))
			{
				remName=cookies[i].getValue();
				if(remName==null) remName="";
				else remName=URLDecoder.decode(remName,"UTF-8");
				c++;
			}
			else if(cookies[i].getName().equals("password"))
			{
				remPassword=cookies[i].getValue();
				if(remPassword==null) remPassword="";
				else remPassword=URLDecoder.decode(remPassword,"UTF-8");
				c++;
			}
		}
	}
	
	
%>		
		<div id="loginResult"></div>
		
		<form name="textForm" method="post" role="form" class="form-horizontal" id="ajaxForm">
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">用户名</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="name" placeholder="请输入用户名" value="<%=remName %>">
					</div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">密码</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" name="password" placeholder="请输入密码" value="<%=remPassword %>">
					</div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">验证码</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="code" placeholder="请输入验证码">
					</div>
				</div>			
			</div>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label"></label>
					<div class="col-sm-10">
						<img style="cursor:hand" name="imgValidate" src="validate.jsp" onclick="refresh()">
					</div>
				</div>			
			</div>
			
			<div class="form-group">
		      <div class="col-sm-offset-3 col-sm-5">
		      	<div class="col-sm-offset-2 col-sm-10">
		         	<div class="checkbox">
			            <label>
			               <input name="rem" type="checkbox" value="yes"> 记住用户名和密码
			            </label>
		         	</div>
		         </div>	
		      </div>
		   </div>
		   
		   <div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<div class="col-sm-offset-2 col-sm-10">
					<div class="btn-group">
						<button type="button" class="btn btn-default" onclick="sub()">登陆</button>
						<button type="button" class="btn btn-default" onclick="location.href='register.jsp'">注册</button>
					</div>
				</div>			
			</div>
			</div>
			
		</form>
	</body>
</html>