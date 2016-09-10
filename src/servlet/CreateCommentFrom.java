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
import javax.servlet.http.HttpSession;

import beans.Dao;

public class CreateCommentFrom extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	public CreateCommentFrom() {
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

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String sql=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		HttpSession session=request.getSession();
		
		String id=(String)session.getAttribute("id");
		if(id==null || id.equals("")) response.sendRedirect("/S2013150064/login.jsp");
		
		String mes_num=request.getParameter("mes_num");
		String from_id=request.getParameter("fid");
		if(mes_num!=null && !mes_num.equals("") && from_id!=null && !from_id.equals(""))
		{
			try
			{
				out.print("<form method=\"post\" role=\"form\" class=\"form-horizontal\" id=\""+from_id+"\">");
				out.print("<input type=\"hidden\" name=\"mes_num\" value=\""+mes_num+"\">");
				out.print("<div class=\"form-group\">");
				out.print("<textarea name=\"text\" class=\"form-control\" rows=\"4\" placeholder=\"请输入评论内容(长度<65535)\"></textarea>");
				out.print("</div>");
				out.print("<div class=\"form-group\">");
				out.print("<div class=\"pull-right\"><button type=\"button\" class=\"btn btn-default\" onclick=\"subComment('"+from_id+"','"+mes_num+"')\">提交</button></div>");
				out.print("</div>");
				out.print("</form>");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		out.flush();
		out.close();
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

		doGet(request,response);
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
