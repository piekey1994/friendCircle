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

public class Apply extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	public Apply() {
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
		
		String apply_num=request.getParameter("num");
		String apply_id=(String)session.getAttribute("id");
		if(apply_id==null || apply_id.equals("")) response.sendRedirect("/S2013150064/login.jsp");
		String apply_who="";
		
		if(apply_num!=null && !apply_num.equals(""))
		{
			try
			{
				sql="SELECT WHO FROM FC_APPLY_FRIEND WHERE NUM=? AND ID=? AND READ_OK=0";
				ps = conn.prepareStatement(sql);
				ps.setString(1, apply_num);
				ps.setString(2, apply_id);
				rs = ps.executeQuery();
				
				if(rs.next())
				{
					apply_who=rs.getString("WHO");
					
					sql="UPDATE FC_APPLY_FRIEND,FC_USER SET FC_APPLY_FRIEND.READ_OK=1,FC_USER.APPLY_NUM=FC_USER.APPLY_NUM-1 WHERE FC_APPLY_FRIEND.NUM=? AND FC_USER.ID=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, apply_num);
					ps.setString(2, apply_id);
					ps.executeUpdate();
					
					sql = "INSERT INTO FC_RELATIONSHIP(ID,WHO) VALUES(?,?),(?,?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, apply_id);
					ps.setString(2, apply_who);
					ps.setString(3, apply_who);
					ps.setString(4, apply_id);
					ps.executeUpdate();
				
					
					out.print("添加成功");
				}	
				else out.print("添加失败");
			}catch(Exception e)
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
