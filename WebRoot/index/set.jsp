<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.util.*" import="beans.*"%>

<%
	String id=(String)session.getAttribute("id");
	Dao dao=new Dao();
	Connection conn=dao.getConn();
	String sql;
	PreparedStatement ps;
	ResultSet rs;
	String name="";
	String avatar="";
	
	sql="SELECT NAME,AVATAR FROM FC_USER WHERE ID=?";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	rs = ps.executeQuery();
	if(rs.next())
	{
		name=rs.getString("NAME");
		avatar=rs.getString("AVATAR");
	}
	
%>

<html>
  <head>
	<link rel="stylesheet" href="/S2013150064/CSS/bootstrap.min.css">
		<style type="text/css">
	    <!--
	    	a:link {color: #66CCFF}  
			a:hover {text-decoration:underline ; cursor:pointer; color: #66CCFF} 
			a:active {color: #0000FF}  	
			body,button, input, select, textarea,h1 ,h2, h3, h4, h5, h6 { font-family: Microsoft YaHei,'宋体' , Tahoma, Helvetica, Arial, "\5b8b\4f53", sans-serif;}
	    -->
	  	</style>
	  	<script type="text/javascript" src="/S2013150064/JS/jquery.min.js"></script>
	  	<script type="text/javascript">
		  	var webSocket = new WebSocket('ws://piekey.ecs12.tomcats.pw/S2013150064/nativeSocket');  
		  	webSocket.onerror = function(event) {  
		  		//alert(event.data);
		  	};  
		  	webSocket.onopen = function(event) {  
		  		 webSocket.send(<%=id %>); 
		  	};  
		  	webSocket.onmessage = function(event) {  
		  	    var num1=event.data.substr(0,1);
		  	    var num2=event.data.substr(2,1);
		  	    if(num1!="0") $("#nativeNum").html(num1);
		  	    else $("#nativeNum").html("");
		  	    if(num2!="0") $("#applyNum").html(num2);
		  	    else $("#applyNum").html("");
		  	};  
	  	</script>
        <script type="text/javascript">  
        	function subAvatar()
        	{
        		var obj = document.getElementById('inputfile');  
                if (obj.value == '') {  
                    alert('请选择要上传的文件');  
                    return false;  
                }  
                return true;  
        	}
        	function subPassword()
        	{
        		oldPassword=document.passwordForm.oldPassword.value;
        		newPassword=document.passwordForm.newPassword.value;
        		newPassword2=document.passwordForm.newPassword2.value;
				if(oldPassword.length==0)
				{
					alert("请输入您的旧密码");
					document.passwordForm.oldPassword.focus();
					return false;
				}
				if(newPassword.length==0)
				{
					alert("请输入您的新密码");
					document.passwordForm.newPassword.focus();
					return false;
				}
				if(newPassword2.length==0)
				{
					alert("请再输入您的新密码");
					document.passwordForm.newPassword2.focus();
					return false;
				}
				if(newPassword!=newPassword2)
				{
					alert("两次密码不一致");
					document.passwordForm.newPassword2.focus();
					return false;
				}
				$.ajax({
					cache: false,
					type: "POST",
					url:"/S2013150064/servlet/SetPassword",	
					data:$('#ajaxForm').serialize(),	
					async: true,
					error: function(request) {
					alert("发送请求失败！");
					},
					success: function(data) {
						$("#passwordResult").html(data);
					}
					});
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
		         <li><a href="/S2013150064/index/index.jsp">主页</a></li>
		         <li><a href="/S2013150064/index/native.jsp">通知<span class="badge" id="nativeNum"></span></a></li>
		         <li><a href="/S2013150064/index/friend.jsp">好友<span class="badge" id="applyNum"></span></a></li>
		         <li class="active"><a href="/S2013150064/index/set.jsp">设置</a></li>
		         <li><a href="/S2013150064/index/quit.jsp">注销</a></li>
		      </ul>
		   </div>
		</nav>
		
		<form name="textForm" method="post" role="form" class="form-horizontal" action="/S2013150064/servlet/SetAvatar" enctype="multipart/form-data">
		
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<div class="media">
					   <a class="pull-left" href="#">
					      <img height="100" width="100" class="media-object" src="<%=avatar %>" alt="头像" title="头像">
					   </a>
					   <div class="media-body">
					      <h4 class="media-heading"><%=name %></h4>
					      <label for="inputfile">更改头像（支持jpg、png、bmp、gif，大小超过5MB会被忽略）</label>
      						<input type="file" name="avatarFile" id="inputfile" accept=".jpeg,.jpg,.png,.bmp,.gif">
      						<div class="pull-right"><button type="submit" class="btn btn-default" onclick="return subAvatar()">提交</button></div>
					   </div>
					</div>
				</div>			
			</div>
			</form>
			
			<div class="col-sm-offset-3 col-sm-5">
				<hr style=" height:2px;border:none;border-top:2px solid #b4b4b4;" />
			</div>
			
			<form name="passwordForm" method="post" role="form" class="form-horizontal" id="ajaxForm">
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-5">
						<label class="col-sm-2 control-label">旧密码</label>
						<div class="col-sm-8">
							<input type="password" class="form-control" name="oldPassword" placeholder="请输入旧密码">
						</div>
					</div>			
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-5">
						<label class="col-sm-2 control-label">新密码</label>
						<div class="col-sm-8">
							<input type="password" class="form-control" name="newPassword" placeholder="请输入新密码">
						</div>
					</div>			
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-5">
						<label class="col-sm-2 control-label">重复新密码</label>
						<div class="col-sm-8">
							<input type="password" class="form-control" name="newPassword2" placeholder="请再输入新密码">
						</div>
					</div>			
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-5">
						<div class="pull-right"><button type="button" class="btn btn-default" onclick="subPassword()">提交</button></div>
					</div>			
				</div>
			</form>
			
			<div class="col-sm-offset-3 col-sm-5">
				<div id="passwordResult"></div>
			</div>
			
		
  </body>
  
</html>