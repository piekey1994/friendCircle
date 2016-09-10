package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
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

public class SetPassword extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	public SetPassword() {
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
	
	//密码是否符合要求
			public boolean passwordOK(String password)
			{
				if(password.length()<6 || password.length()>20) return false;
				if(password.indexOf(" ")!=-1) return false;
				if(password.indexOf("<")!=-1) return false;
				if(password.indexOf(">")!=-1) return false;
				if(password.indexOf("&")!=-1) return false;
				if(password.indexOf("\n")!=-1) return false;
				if(password.indexOf("@")!=-1) return false;
				return true;
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
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		if(id==null || id.equals("")) response.sendRedirect("/S2013150064/login.jsp");
		
		String oldPassword=request.getParameter("oldPassword");
		String newPassword=request.getParameter("newPassword");
		String newPassword2=request.getParameter("newPassword2");
		if(oldPassword!=null && !oldPassword.equals("") && newPassword!=null && !newPassword.equals("") && newPassword2!=null && !newPassword2.equals(""))
		{
			if(!newPassword.equals(newPassword2)) out.print("<div style=\"color: #FF0000\"><h4>两次密码不一致</h4></div>");
			else
			{
				if(!passwordOK(newPassword)) out.print("<div style=\"color: #FF0000\"><h4>新密码不合法</h4></div>");
				else
				{
					try
					{
						String name="";
						sql="SELECT NAME FROM FC_USER WHERE ID=?";
						ps = conn.prepareStatement(sql);
						ps.setString(1, id);
						rs = ps.executeQuery();
						if(rs.next()) 
						{
							name=rs.getString("NAME");
						}
						oldPassword=(new BigInteger(ToolBean.encryptSHA((oldPassword+name).getBytes()))).toString(16);
						newPassword=(new BigInteger(ToolBean.encryptSHA((newPassword+name).getBytes()))).toString(16);
						sql="UPDATE FC_USER SET PASSWORD=PASSWORD(?) WHERE PASSWORD=PASSWORD(?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, newPassword);
						ps.setString(2, oldPassword);
						if(ps.executeUpdate()==1)
						{
							out.print("<div style=\"color: #0db300\"><h4>更新密码成功</h4></div>");
						}
						else out.print("<div style=\"color: #FF0000\"><h4>旧密码错误</h4></div>");
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
		}
		else out.print("<div style=\"color: #FF0000\"><h4>输入有误</h4></div>");
		
		
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
