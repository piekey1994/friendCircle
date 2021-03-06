package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Dao;

public class Search extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	
	public Search() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
		try {
			dao.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String sql=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sname=request.getParameter("sname");
		String avatar="";
		String id="";
		
		if(sname!=null && !sname.equals(""))
		{
			try
			{
				sql="SELECT AVATAR,ID FROM FC_USER WHERE NAME=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, sname);
				rs = ps.executeQuery();
				if(rs.next()) 
				{
					avatar=rs.getString("AVATAR");
					id=rs.getString("ID");
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			if(!avatar.equals("")) out.print("<h4><img height=\"50\" width=\"50\" src=\""+avatar+"\"  class=\"img-thumbnail\"><span>&nbsp;"+sname+"&nbsp;&nbsp;&nbsp;&nbsp;</span><font color=\"#66CCFF\"><small><span id=\"addFriend\"><a onclick=\"add("+id+")\">添加好友</a></span></small></font></h4>");
			else out.print("<div style=\"color: #FF0000\"><h4>没有找到该用户</h4></div></div>");
		}	
		
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		try {
			dao=new Dao();
			conn=dao.getConn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
