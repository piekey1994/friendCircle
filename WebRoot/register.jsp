<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

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
				password2=document.textForm.password2.value;
				email=document.textForm.email.value;
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
				if(password2.length==0)
				{
					alert("请再输入您的密码");
					document.textForm.password2.focus();
					return false;
				}
				if(email.length==0)
				{
					alert("请输入您的邮箱");
					document.textForm.email.focus();
					return false;
				}
				if(password2!=password)
				{
					alert("两次密码不一致，请重新输入");
					document.textForm.password.value="";
					document.textForm.password2.value="";
					document.textForm.password.focus();
					return false;
				}
				$.ajax({
					cache: false,
					type: "POST",
					url:"servlet/Register",	
					data:$('#ajaxForm').serialize(),
					async: true,
					error: function(request) {
					alert("发送请求失败！");
					},
					success: function(data) {
						cleanForm();
						$("#registerResult").html(data);						
					}
					});
			}
			function cleanForm()
			{
				document.textForm.reset();
				$("#registerResult").html("");	
				$("#nameCheck").html("");
				$("#passwordCheck").html("");
				$("#password2Check").html("");
				$("#emailCheck").html("");
			}
			function checkInput(event)
			{
				if(event.target.name=="name")
				{
					 $.get("servlet/Check?name="+event.target.value, function(result){
						 $("#nameCheck").html(result);
						  });
				}
				else if(event.target.name=="password")
				{
					$.get("servlet/Check?password="+event.target.value, function(result){
						 $("#passwordCheck").html(result);
						  });
				}
				else if(event.target.name=="password2")
				{
					$("#password2Check").html("<div style=\"color: #FF0000\">检查中</div>");
					if(document.textForm.password.value==document.textForm.password2.value)
					{
						$("#password2Check").html("<div style=\"color: #0db300\">密码一致</div>");
					}
					else $("#password2Check").html("<div style=\"color: #FF0000\">两次密码不一致</div>");
				}
				else if(event.target.name=="email")
				{
					$.get("servlet/Check?email="+event.target.value, function(result){
						 $("#emailCheck").html(result);
						  });
				}
			}
		</script>
	</head>
	
	<body>
		<nav class="navbar navbar-default" role="navigation">
		   <div class="navbar-header">
		      <a class="navbar-brand" href="#">朋友圈</a>
		   </div>
		   <div>
		      <ul class="nav navbar-nav">
		         <li><a href="login.jsp">登陆</a></li>
		         <li class="active"><a href="register.jsp">注册</a></li>
		      </ul>
		   </div>
		</nav>
			
		<div id="registerResult"></div>
		
		<form name="textForm" method="post" role="form" class="form-horizontal" id="ajaxForm">
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">用户名</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="name"  oninput="checkInput(event)" placeholder="请输入您的用户名(4&lt;长度&lt;20,不能包含特殊字符)">
					</div>
					<div id="nameCheck"></div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">密码</label>
					<div class="col-sm-8">
						<input type="password" class="form-control" name="password" oninput="checkInput(event)" placeholder="请输入您的密码(6&lt;长度&lt;20)">
					</div>
					<div id="passwordCheck"></div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">确认密码</label>
					<div class="col-sm-8">
						<input type="password" class="form-control" name="password2" oninput="checkInput(event)" placeholder="请再输入您的密码">
					</div>
					<div id="password2Check"></div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">邮箱</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="email" oninput="checkInput(event)" placeholder="请输入您的邮箱">
					</div>
					<div id="emailCheck"></div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<div class="col-sm-offset-2 col-sm-10">
						<div class="btn-group">
							<button type="button" class="btn btn-default" onclick="sub()">提交</button>
							<button type="button" class="btn btn-default" onclick="cleanForm()">重置</button>
						</div>
					</div>			
				</div>
			</div>
			
		</form>
		
		<div class="col-sm-offset-3 col-sm-5">
		<div class="col-sm-offset-2 col-sm-10">
			<div style="color: #66CCFF"><h5><a href="login.jsp">返回登陆界面</a></h5></div>
		</div>
		</div>
	</body>
</html>