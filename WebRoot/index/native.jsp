<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.util.*" import="beans.*"%>

<%
	String id=(String)session.getAttribute("id");
	Dao dao=new Dao();
	Connection conn=dao.getConn();
	String sql;
	PreparedStatement ps;
	ResultSet rs;
	sql="UPDATE FC_USER SET NATIVE_NUM=0 WHERE ID=?";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	ps.executeUpdate();
			
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
        	function seePraise(num,mes_num)
        	{
        		$.get("/S2013150064/servlet/SeeNative?praise="+num, function(result){
        			location.href="/S2013150064/index/text.jsp?num="+mes_num;
					  });
        		
        	}
        	function seeComment(num,mes_num)
        	{
        		$.get("/S2013150064/servlet/SeeNative?comment="+num, function(result){
        			location.href="/S2013150064/index/text.jsp?num="+mes_num;
					  });
        	}
        	function seeRemind(num,mes_num)
        	{
        		$.get("/S2013150064/servlet/SeeNative?remind="+num, function(result){
        			location.href="/S2013150064/index/text.jsp?num="+mes_num;
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
		         <li class="active"><a href="/S2013150064/index/native.jsp">通知<span class="badge" id="nativeNum"></span></a></li>
		         <li><a href="/S2013150064/index/friend.jsp">好友<span class="badge" id="applyNum"></span></a></li>
		         <li><a href="/S2013150064/index/set.jsp">设置</a></li>
		         <li><a href="/S2013150064/index/quit.jsp">注销</a></li>
		      </ul>
		   </div>
		</nav>
		
		<div class="col-sm-offset-3 col-sm-6">
			<div><h4>点赞：</h4></div>
<%
	sql="SELECT FC_PRAISE.NUM,FC_PRAISE.MES_NUM,FC_USER.NAME,FC_USER.AVATAR FROM FC_PRAISE,FC_USER,FC_RELATIONSHIP WHERE FC_PRAISE.ID=? AND FC_PRAISE.READ_OK=0 AND FC_PRAISE.WHO=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 AND FC_USER.ID=FC_PRAISE.WHO ORDER BY FC_PRAISE.TIME DESC";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	ps.setString(2, id);
	rs = ps.executeQuery();
	while(rs.next())
	{
		String praise_num=rs.getString("NUM");
		String mes_num=rs.getString("MES_NUM");
		String name=rs.getString("NAME");
		String avatar=rs.getString("AVATAR");
		out.print("<h4><img height=\"50\" width=\"50\" src=\""+avatar+"\"  class=\"img-thumbnail\"><span>&nbsp;"+name+"&nbsp;&nbsp;&nbsp;&nbsp;<small><a onclick=\"seePraise('"+praise_num+"','"+mes_num+"')\">点赞了你</a></small></span></h4>");
	}
%>		
			<hr style=" height:2px;border:none;border-top:2px solid #b4b4b4;" />
		</div>
		
		<div class="col-sm-offset-3 col-sm-6">
			<div><h4>评论：</h4></div>
<%
	sql="SELECT FC_COMMENT.NUM,FC_COMMENT.MES_NUM,FC_USER.NAME,FC_USER.AVATAR FROM FC_COMMENT,FC_USER,FC_RELATIONSHIP WHERE FC_COMMENT.ID=? AND FC_COMMENT.READ_OK=0 AND FC_COMMENT.WHO=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 AND FC_USER.ID=FC_COMMENT.WHO ORDER BY FC_COMMENT.TIME DESC";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	ps.setString(2, id);
	rs = ps.executeQuery();
	while(rs.next())
	{
		String comment_num=rs.getString("NUM");
		String mes_num=rs.getString("MES_NUM");
		String name=rs.getString("NAME");
		String avatar=rs.getString("AVATAR");
		out.print("<h4><img height=\"50\" width=\"50\" src=\""+avatar+"\"  class=\"img-thumbnail\"><span>&nbsp;"+name+"&nbsp;&nbsp;&nbsp;&nbsp;<small><a onclick=\"seeComment('"+comment_num+"','"+mes_num+"')\">评论了你</a></small></span></h4>");
	}
%>		
			<hr style=" height:2px;border:none;border-top:2px solid #b4b4b4;" />
		</div>
		
		<div class="col-sm-offset-3 col-sm-6">
			<div><h4>@你：</h4></div>
<%
	sql="SELECT FC_REMIND.NUM,FC_REMIND.MES_NUM,FC_USER.NAME,FC_USER.AVATAR FROM FC_REMIND,FC_USER,FC_RELATIONSHIP WHERE FC_REMIND.WHO=? AND FC_REMIND.READ_OK=0 AND FC_REMIND.ID=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 AND FC_USER.ID=FC_REMIND.ID ORDER BY FC_REMIND.TIME DESC";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	ps.setString(2, id);
	rs = ps.executeQuery();
	while(rs.next())
	{
		String remind_num=rs.getString("NUM");
		String mes_num=rs.getString("MES_NUM");
		String name=rs.getString("NAME");
		String avatar=rs.getString("AVATAR");
		out.print("<h4><img height=\"50\" width=\"50\" src=\""+avatar+"\"  class=\"img-thumbnail\"><span>&nbsp;"+name+"&nbsp;&nbsp;&nbsp;&nbsp;<small><a onclick=\"seeRemind('"+remind_num+"','"+mes_num+"')\">提醒了你</a></small></span></h4>");
	}
%>		
		</div>
		
  </body>
  
</html>