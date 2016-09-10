<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.util.*" import="beans.*"%>
<%
	String id=(String)session.getAttribute("id");
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
			function search()
			{
				sname=document.searchForm.sname.value;
				if(sname.length==0)
				{
					alert("请输入您要搜索的好友昵称");
					document.searchForm.sname.focus();
					return false;
				}
				$.ajax({
					cache: false,
					type: "POST",
					url:"/S2013150064/servlet/Search",	
					data:$('#ajaxForm').serialize(),	
					async: true,
					error: function(request) {
					alert("发送请求失败！");
					},
					success: function(data) {
						$("#searchResult").html(data);
					}
					});
			}
			function add(who)
			{
				$.get("/S2013150064/servlet/AddFriend?id="+who, function(result){
					 $("#addFriend").html(result);
					  });
			}
			function apply(num,ajaxDiv)
			{
				$.get("/S2013150064/servlet/Apply?num="+num, function(result){
					 $("#"+ajaxDiv).html(result);
					  });
			}
			function refuse(num,ajaxDiv)
			{
				$.get("/S2013150064/servlet/Refuse?num="+num, function(result){
					 $("#"+ajaxDiv).html(result);
					  });
			}
			function del(num,ajaxDiv)
			{
				$.get("/S2013150064/servlet/DelFriend?num="+num, function(result){
					 $("#"+ajaxDiv).html(result);
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
		         <li class="active"><a href="/S2013150064/index/friend.jsp">好友<span class="badge" id="applyNum"></span></a></li>
		         <li><a href="/S2013150064/index/set.jsp">设置</a></li>
		         <li><a href="/S2013150064/index/quit.jsp">注销</a></li>
		      </ul>
		   </div>
		</nav>
		
		<form name="searchForm" method="post" role="form" class="form-horizontal" id="ajaxForm">
		
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-5">
					<label class="col-sm-2 control-label">搜索好友</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" name="sname" placeholder="请输入好友昵称">
					</div>
					<button type="button" class="btn btn-default" onclick="search()">搜索</button>
				</div>			
			</div>
		</form>
		
		<div class="col-sm-offset-4 col-sm-5">
			<div id="searchResult"></div>
		</div>	
		
		<div class="col-sm-offset-3 col-sm-5">
		<hr style=" height:2px;border:none;border-top:2px solid #b4b4b4;" />
			<label class="col-sm-2 control-label">申请列表</label>
			<div class="col-sm-12">
<%
	Dao dao=new Dao();
	Connection conn=dao.getConn();
	String sql;
	PreparedStatement ps;
	ResultSet rs;
	Vector<String> applyName=new Vector<String>();
	Vector<String> applyAvatar=new Vector<String>();
	Vector<String> applyNum=new Vector<String>();
	sql="SELECT FC_USER.NAME,FC_USER.AVATAR,FC_APPLY_FRIEND.NUM FROM FC_USER,FC_APPLY_FRIEND WHERE FC_APPLY_FRIEND.ID=? AND FC_APPLY_FRIEND.WHO=FC_USER.ID AND FC_APPLY_FRIEND.READ_OK=0";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	rs = ps.executeQuery();
	while(rs.next())
	{
		applyName.add(rs.getString("NAME"));
		applyAvatar.add(rs.getString("AVATAR"));
		applyNum.add(rs.getString("NUM"));
	}
	for(int i=0;i<applyName.size();i++)
	{
		out.println("<h4><img height=\"50\" width=\"50\" src=\""+applyAvatar.get(i)+"\"  class=\"img-thumbnail\"><span>&nbsp;"+applyName.get(i)+"&nbsp;&nbsp;&nbsp;&nbsp;</span><font color=\"#66CCFF\"><small><span id=\"applyFriend"+i+"\"><a onclick=\"apply("+applyNum.get(i)+",'applyFriend"+i+"')\">同意</a>&nbsp;&nbsp;<a onclick=\"refuse("+applyNum.get(i)+",'applyFriend"+i+"')\">拒绝</a></span></small></font></h4>");
	}
%>
			</div>
		</div>		
		
		<div class="col-sm-offset-3 col-sm-5">
		<hr style=" height:2px;border:none;border-top:2px solid #b4b4b4;" />
			<label class="col-sm-2 control-label">好友列表</label>
			<div class="col-sm-12">
<%
	Vector<String> friendName=new Vector<String>();
	Vector<String> relationshipNum=new Vector<String>();
	Vector<String> friendAvatar=new Vector<String>();
	sql="SELECT FC_USER.NAME,FC_RELATIONSHIP.NUM,FC_USER.AVATAR FROM FC_USER,FC_RELATIONSHIP WHERE FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.WHO=FC_USER.ID AND FC_RELATIONSHIP.HIDEN=0 AND FC_RELATIONSHIP.ID!=FC_RELATIONSHIP.WHO";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	rs = ps.executeQuery();
	while(rs.next())
	{
		friendName.add(rs.getString("NAME"));
		relationshipNum.add(rs.getString("NUM"));
		friendAvatar.add(rs.getString("AVATAR"));
	}
	for(int i=0;i<friendName.size();i++)
	{
		out.println("<h4><img height=\"50\" width=\"50\" src=\""+friendAvatar.get(i)+"\"  class=\"img-thumbnail\"><span>&nbsp;"+friendName.get(i)+"&nbsp;&nbsp;&nbsp;&nbsp;</span><font color=\"#66CCFF\"><small><span id=\"delFriend"+i+"\"><a onclick=\"del("+relationshipNum.get(i)+",'delFriend"+i+"')\">删除</a></span></small></font></h4>");
	}
%>
			</div>
		</div>		
		
  </body>
  
</html>