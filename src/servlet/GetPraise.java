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

public class GetPraise extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	public GetPraise() {
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
				sql="SELECT FC_USER.AVATAR,FC_USER.NAME FROM FC_USER,FC_PRAISE,FC_RELATIONSHIP WHERE FC_PRAISE.MES_NUM=? AND FC_PRAISE.WHO=FC_USER.ID AND FC_PRAISE.WHO=FC_RELATIONSHIP.WHO AND FC_RELATIONSHIP.ID=? AND FC_RELATIONSHIP.HIDEN=0 ORDER BY FC_PRAISE.TIME DESC";
				ps = conn.prepareStatement(sql);
				ps.setString(1, mes_num);
				ps.setString(2, id);
				rs = ps.executeQuery();
				while(rs.next())
				{
					String avatar=rs.getString("AVATAR");
					String name=rs.getString("NAME");
					out.print("<img height=\"40\" width=\"40\" class=\"img-thumbnail\" src=\""+avatar+"\" alt=\""+name+"\" title=\""+name+"\">&nbsp;");
				}
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
