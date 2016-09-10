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

public class AddFriend extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	public AddFriend() {
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
		
		String id=request.getParameter("id");
		String who=(String)session.getAttribute("id");
		if(who==null || who.equals("")) response.sendRedirect("/S2013150064/login.jsp");
		
		if(id!=null && !id.equals("") && who!=null && !who.equals(""))
		{
			if(id.equals(who)) out.print("您不能添加自己为好友");
			else
			{
				try
				{
					sql="SELECT WHO FROM FC_RELATIONSHIP WHERE WHO=? AND ID=? AND HIDEN=0";
					ps = conn.prepareStatement(sql);
					ps.setString(1, who);
					ps.setString(2, id);
					rs = ps.executeQuery();
					if(rs.next()) out.print("他已经是您的好友了");
					else
					{
						String ip=ToolBean.getIpAddr(request);
						sql = "INSERT INTO FC_APPLY_FRIEND(ID,WHO,IP) VALUES(?,?,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, id);
						ps.setString(2, who);
						ps.setString(3, ip);
						ps.executeUpdate();
						sql="UPDATE FC_USER SET APPLY_NUM=APPLY_NUM+1 WHERE ID = ?";
						ps = conn.prepareStatement(sql);
						ps.setString(1, id);
						ps.executeUpdate();
						
						out.print("发送申请成功");
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
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
