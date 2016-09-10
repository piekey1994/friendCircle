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
import beans.ToolBean;

public class Praise extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	public Praise() {
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
		
		if(mes_num!=null && !mes_num.equals(""))
		{
			try
			{
				sql="SELECT * FROM FC_PRAISE WHERE MES_NUM=? AND WHO=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, mes_num);
				ps.setString(2, id);
				rs = ps.executeQuery();
				if(!rs.next())
				{
					sql="SELECT * FROM FC_RELATIONSHIP WHERE ID=? AND WHO=(SELECT ID FROM FC_TEXT WHERE NUM=?) AND HIDEN=0";
					ps = conn.prepareStatement(sql);
					ps.setString(1, id);
					ps.setString(2, mes_num);
					rs = ps.executeQuery();
					if(rs.next())
					{
						String ip=ToolBean.getIpAddr(request);
						sql="INSERT INTO FC_PRAISE(ID,MES_NUM,WHO,IP) VALUES((SELECT ID FROM FC_TEXT WHERE NUM=?),?,?,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, mes_num);
						ps.setString(2, mes_num);
						ps.setString(3, id);
						ps.setString(4, ip);
						ps.executeUpdate();
						sql="UPDATE FC_USER SET NATIVE_NUM=NATIVE_NUM+1 WHERE ID=(SELECT ID FROM FC_TEXT WHERE NUM=?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, mes_num);
						ps.executeUpdate();
					}
				}
				
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
