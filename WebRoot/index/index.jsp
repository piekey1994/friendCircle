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
	Vector<String> friendName=new Vector<String>();
	Vector<String> friendID=new Vector<String>();
	String seeSomebody="";
	String remindSomebody="";
	
	//获取基本信息
	sql="SELECT NAME,AVATAR FROM FC_USER WHERE ID=?";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	rs = ps.executeQuery();
	if(rs.next())
	{
		name=rs.getString("NAME");
		avatar=rs.getString("AVATAR");
	}
	sql="SELECT FC_USER.NAME,FC_USER.ID FROM FC_USER,FC_RELATIONSHIP WHERE FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.WHO=FC_USER.ID AND FC_RELATIONSHIP.HIDEN=0 AND FC_RELATIONSHIP.ID!=FC_RELATIONSHIP.WHO";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	rs = ps.executeQuery();
	while(rs.next())
	{
		friendName.add(rs.getString("NAME"));
		friendID.add(rs.getString("ID"));
	}
	for (int i = 0; i < friendName.size(); i++) 
	{
		seeSomebody=seeSomebody+"<label class=\\\"checkbox-inline\\\"><input name=\\\"seeID\\\" type=\\\"checkbox\\\"  value=\\\""+friendID.get(i)+"\\\">"+friendName.get(i)+"</label>";
		remindSomebody=remindSomebody+"<label class=\\\"checkbox-inline\\\"><input name=\\\"remindID\\\" type=\\\"checkbox\\\"  value=\\\""+friendID.get(i)+"\\\">"+friendName.get(i)+"</label>";
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
			function sub()
			{
				text=document.textForm.text.value;
				if(text.length==0)
				{
					alert("请输入您的留言");
					document.textForm.text.focus();
					return false;
				}
				return true;
			}
			function somebodyCanSee()
			{
				$("#seeList").html("<%=seeSomebody %>");
			}
			function allCanSee()
			{
				$("#seeList").html("");
			}
			function remindSomebody()
			{
				$("#remindList").html("<%=remindSomebody %>");
			}
			function remindNobody()
			{
				$("#remindList").html("");
			}
			function updatePraise(mes_num,ajaxDiv)
        	{
        		$.get("/S2013150064/servlet/GetPraise?mes_num="+mes_num, function(result){
					 $("#"+ajaxDiv).html(result);
					  });
        	}
        	function delMes(mes_num,text_num)
        	{
        		$.get("/S2013150064/servlet/DelText?mes_num="+mes_num, function(result){
        			if(result=="true")
        			 $("#"+text_num).html("<div style=\"color: #0db300\"><h4>删除成功</h4></div>");
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
        		if(fromText.text.value.length==0)
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
						location.href="/S2013150064/index/index.jsp";
					}
					});
        	}
        	function delCom(com_num,mes_num)
        	{
        		$.get("/S2013150064/servlet/DelComment?com_num="+com_num, function(result){
        			location.href="/S2013150064/index/index.jsp";
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
		         <li class="active"><a href="/S2013150064/index/index.jsp">主页</a></li>
		         <li><a href="/S2013150064/index/native.jsp">通知<span class="badge" id="nativeNum"></span></a></li>
		         <li><a href="/S2013150064/index/friend.jsp">好友<span class="badge" id="applyNum"></span></a></li>
		         <li><a href="/S2013150064/index/set.jsp">设置</a></li>
		         <li><a href="/S2013150064/index/quit.jsp">注销</a></li>
		      </ul>
		   </div>
		</nav>	
		
<%
	String msg=(String)session.getAttribute("msg");
	if(msg!=null)
	{
		out.println(msg);
		session.setAttribute("msg", "");
	}
%>
		<form name="textForm" method="post" role="form" class="form-horizontal" action="/S2013150064/servlet/Publish" enctype="multipart/form-data">
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
				<h4><img height="50" width="50" src="<%=avatar %>"  class="img-thumbnail">
					<span><%=name %>:</span></h4>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
					<textarea name="text" class="form-control" rows="7" id="id_text" placeholder="请输入留言内容(长度<65535)"></textarea>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
					<label for="inputfile">上传照片（可选，支持jpg、png、bmp、gif，大小超过5MB会被忽略）</label>
      				<input type="file" name="file" id="inputfile" accept=".jpeg,.jpg,.png,.bmp,.gif">
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
      				<label class="checkbox-inline">
				      <input type="radio" name="authority" value="all" onclick="allCanSee()" checked> 所有好友可见
				   </label>
				   <label class="checkbox-inline">
				      <input type="radio" name="authority" value="somebody" onclick="somebodyCanSee()"> 部分好友可见
				   </label>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
					<div id="seeList"></div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
      				<label class="checkbox-inline">
				      <input type="radio" name="remind" value="no" onclick="remindNobody()" checked> 不提醒
				   </label>
				   <label class="checkbox-inline">
				      <input type="radio" name="remind" value="somebody" onclick="remindSomebody()"> 提醒谁看
				   </label>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
					<div id="remindList"></div>
				</div>			
			</div>
			
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-6">
					<div class="pull-right"><button type="submit" class="btn btn-default" onclick="return sub()">发布</button></div>
				</div>			
			</div>		
		</form>

		<div class="col-sm-offset-3 col-sm-6">
		<hr style=" height:2px;border:none;border-top:2px solid #b4b4b4;" />
			<ul class="media-list">	
			
<%
	sql="SELECT FC_TEXT.NUM FROM FC_TEXT,FC_RELATIONSHIP WHERE FC_TEXT.HIDEN=0 AND FC_TEXT.ID=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 ORDER BY FC_TEXT.TIME DESC";
	ps = conn.prepareStatement(sql);
	ps.setString(1, id);
	rs = ps.executeQuery();
	int count=0;
	while(rs.next())
	{
		String mes_num=rs.getString("NUM");
		sql="SELECT FC_TEXT.ID,FC_USER.AVATAR,FC_USER.NAME,FC_TEXT.TEXT,FC_TEXT.PICTURE,FC_TEXT.TIME FROM FC_TEXT,FC_USER WHERE FC_TEXT.NUM=? AND FC_TEXT.ID=FC_USER.ID AND (-1 IN (SELECT WHO FROM FC_VISIBLE WHERE MES_NUM=?) OR ? IN (SELECT WHO FROM FC_VISIBLE WHERE MES_NUM=?))";
		ps = conn.prepareStatement(sql);
		ps.setString(1, mes_num);
		ps.setString(2, mes_num);
		ps.setString(3, id);
		ps.setString(4, mes_num);
		ResultSet rs2 = ps.executeQuery();
		if(rs2.next())
		{
			out.print("<script type=\"text/javascript\">updatePraise('"+mes_num+"','praiseDiv"+count+"')</script>");
			String mes_id=rs2.getString("ID");
			String mes_avatar=rs2.getString("AVATAR");
			String mes_name=rs2.getString("NAME");
			String mes_text=rs2.getString("TEXT");
			String mes_picture=rs2.getString("PICTURE");
			String mes_time=ToolBean.getInputTime(rs2.getString("TIME"));
			out.print("<div id=\"text"+count+"\">");
			out.print("<div class=\"panel panel-default\">");
			out.print("<div class=\"panel-heading\">");
			out.print("<h3 class=\"panel-title\">"+mes_time);
			if(id.equals(mes_id))
				out.print("&nbsp;&nbsp;&nbsp;&nbsp;<small><a onclick=\"delMes('"+mes_num+"','text"+count+"')\">删除</a></small>");
			out.print("</h3></div>");
			out.print("<div class=\"panel-body\">");
			out.print("<li class=\"media\">");
			out.print("<a class=\"pull-left\" href=\"#\">");
			out.print("<img height=\"80\" width=\"80\" class=\"media-object\" src=\""+mes_avatar+"\" alt=\"头像\"></a>");
			out.print("<div class=\"media-body\">");
			out.print("<h4 class=\"media-heading\">"+mes_name+"</h4>");
			out.print(mes_text);
			if(!mes_picture.equals("/S2013150064"))
			{
				out.print("<div><img src=\""+mes_picture+"\" alt=\"照片\"></div>");
			}		
			out.print("<div style=\"color: #66CCFF\"><h5>");
			out.print("<a onclick=\"praise('"+mes_num+"','praiseDiv"+count+"')\">点赞</a>&nbsp;&nbsp;&nbsp;<a onclick=\"comment('"+mes_num+"','commentDiv"+count+"','comFrom"+count+"')\">评论</a></h5></div>");
			out.print("<div class=\"col-sm-10\"><div id=\"commentDiv"+count+"\"></div></div>");
			out.print("<div class=\"col-sm-12\"><img src=\"/S2013150064/pic/love.png\">&nbsp;<span id=\"praiseDiv"+count+"\"></span></div><div>&nbsp;</div>");	
			sql="SELECT FC_COMMENT.NUM,FC_USER.NAME,FC_COMMENT.TEXT,FC_COMMENT.WHO FROM FC_USER,FC_COMMENT,FC_RELATIONSHIP WHERE FC_COMMENT.MES_NUM=? AND FC_COMMENT.HIDEN=0 AND FC_COMMENT.WHO=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 AND FC_COMMENT.WHO=FC_USER.ID ORDER BY FC_COMMENT.TIME DESC";
			ps = conn.prepareStatement(sql);
			ps.setString(1, mes_num);
			ps.setString(2, id);
			rs2 = ps.executeQuery();
			while(rs2.next())
			{
				out.print("<div class=\"col-sm-12\">");
				String comment_num=rs2.getString("NUM");
				String comment_name=rs2.getString("NAME");
				String comment_text=rs2.getString("TEXT");
				String comment_id=rs2.getString("WHO");
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
		count++;
	}
%>
			
			</ul>
		</div>		
		
  </body>
  
</html>
