<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page language="java" import="java.sql.*" import="java.util.*" import="beans.*"%>

<%
	String id=(String)session.getAttribute("id");
	Dao dao=new Dao();
	Connection conn=dao.getConn();
	String sql;
	PreparedStatement ps;
	ResultSet rs;		
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
			img {max-width: 100%;}
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
        	function updatePraise(mes_num,ajaxDiv)
        	{
        		$.get("/S2013150064/servlet/GetPraise?mes_num="+mes_num, function(result){
					 $("#"+ajaxDiv).html(result);
					  });
        	}
        	function delMes(mes_num)
        	{
        		$.get("/S2013150064/servlet/DelText?mes_num="+mes_num, function(result){
        			location.href="/S2013150064/index/text.jsp?num="+mes_num;
					  });
        	}
        	function praise(mes_num,ajaxDiv)
        	{
        		$.get("/S2013150064/servlet/Praise?mes_num="+mes_num, function(result){
        			updatePraise(mes_num,ajaxDiv);
					  });
        	}
        	function comment(mes_num,ajaxDiv,from_id)
        	{
        		$.get("/S2013150064/servlet/CreateCommentFrom?mes_num="+mes_num+"&fid="+from_id, function(result){
        			$("#"+ajaxDiv).html(result);
					  });
        	}
        	function subComment(from_id,mes_num)
        	{
        		fromText=document.getElementById(from_id);
        		if(fromText.length==0)
				{
					alert("请输入您的评论");
					fromText.text.focus();
					return false;
				}
        		$.ajax({
					cache: false,
					type: "POST",
					url:"/S2013150064/servlet/Comment",	
					data:$('#'+from_id).serialize(),
					async: true,
					error: function(request) {
					alert("发送请求失败！");
					},
					success: function(data) {
						location.href="/S2013150064/index/text.jsp?num="+mes_num;
					}
					});
        	}
        	function delCom(com_num,mes_num)
        	{
        		$.get("/S2013150064/servlet/DelComment?com_num="+com_num, function(result){
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
		<div style="color: #66CCFF"><h4><a href="/S2013150064/index/native.jsp">返回通知页面</a></h4></div>
<%
	String mes_num=request.getParameter("num");
	if(mes_num!=null && !mes_num.equals(""))
	{
		sql="SELECT FC_TEXT.ID,FC_USER.AVATAR,FC_USER.NAME,FC_TEXT.TEXT,FC_TEXT.PICTURE,FC_TEXT.TIME FROM FC_USER,FC_TEXT,FC_RELATIONSHIP WHERE FC_TEXT.HIDEN=0 AND FC_TEXT.ID=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 AND FC_TEXT.NUM=? AND FC_USER.ID=FC_TEXT.ID AND (-1 IN (SELECT WHO FROM FC_VISIBLE WHERE MES_NUM=?) OR ? IN (SELECT WHO FROM FC_VISIBLE WHERE MES_NUM=?))";
		ps = conn.prepareStatement(sql);
		ps.setString(1, id);
		ps.setString(2, mes_num);
		ps.setString(3, mes_num);
		ps.setString(4, id);
		ps.setString(5, mes_num);
		rs = ps.executeQuery();
		if(rs.next())
		{
			out.print("<script type=\"text/javascript\">updatePraise('"+mes_num+"','praiseDiv')</script>");
			String mes_id=rs.getString("ID");
			String avatar=rs.getString("AVATAR");
			String name=rs.getString("NAME");
			String text=rs.getString("TEXT");
			String picture=rs.getString("PICTURE");
			String time=ToolBean.getInputTime(rs.getString("TIME"));
			out.print("<div class=\"panel panel-default\">");
			out.print("<div class=\"panel-heading\">");
			out.print("<h3 class=\"panel-title\">"+time);
			if(id.equals(mes_id))
				out.print("&nbsp;&nbsp;&nbsp;&nbsp;<small><a onclick=\"delMes("+mes_num+")\">删除</a></small>");
			out.print("</h3></div>");
			out.print("<div class=\"panel-body\">");
			out.print("<div class=\"media\">");
			out.print("<a class=\"pull-left\" href=\"#\">");
			out.print("<img height=\"80\" width=\"80\" class=\"media-object\" src=\""+avatar+"\" alt=\"头像\"></a>");
			out.print("<div class=\"media-body\">");
			out.print("<h4 class=\"media-heading\">"+name+"</h4>");
			out.print(text);
			if(!picture.equals("/S2013150064"))
			out.print("<div><img src=\""+picture+"\" alt=\"照片\">");
			out.print("<div style=\"color: #66CCFF\"><h5>");
			out.print("<a onclick=\"praise('"+mes_num+"','praiseDiv')\">点赞</a>&nbsp;&nbsp;&nbsp;<a onclick=\"comment('"+mes_num+"','commentDiv','comFrom')\">评论</a></h5></div>");
			out.print("<div class=\"col-sm-10\"><div id=\"commentDiv\"></div></div>");
			out.print("<div class=\"col-sm-12\"><img src=\"/S2013150064/pic/love.png\">&nbsp;<span id=\"praiseDiv\"></span></div></div><div>&nbsp;</div>");	
			sql="SELECT FC_COMMENT.NUM,FC_USER.NAME,FC_COMMENT.TEXT,FC_COMMENT.WHO FROM FC_USER,FC_COMMENT,FC_RELATIONSHIP WHERE FC_COMMENT.MES_NUM=? AND FC_COMMENT.HIDEN=0 AND FC_COMMENT.WHO=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 AND FC_COMMENT.WHO=FC_USER.ID ORDER BY FC_COMMENT.TIME DESC";
			ps = conn.prepareStatement(sql);
			ps.setString(1, mes_num);
			ps.setString(2, id);
			rs = ps.executeQuery();
			while(rs.next())
			{
				out.print("<div class=\"col-sm-12\">");
				String comment_num=rs.getString("NUM");
				String comment_name=rs.getString("NAME");
				String comment_text=rs.getString("TEXT");
				String comment_id=rs.getString("WHO");
				out.print("<div class=\"panel panel-default\"><div class=\"panel-heading\"><h3 class=\"panel-title\">"+comment_name);
				if(id.equals(comment_id))
				{
					out.print("&nbsp;&nbsp;&nbsp;&nbsp;<small><a onclick=\"delCom('"+comment_num+"','"+mes_num+"')\">删除</a></small>");
				}
				out.print("</h3></div><div class=\"panel-body\">"+comment_text+"</div></div>");			
				out.print("</div>");
			}
			out.print("</div></div></div></div>");
			
		}
	}
%>		
			
		</div>
		
		
  </body>
  
</html>