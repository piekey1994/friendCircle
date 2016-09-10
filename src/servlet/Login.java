package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Dao;
import beans.ToolBean;

public class Login extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	
	public Login() {
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
			throws ServletException, IOException{

		
		
		HttpSession session=request.getSession();
		String randStr=(String)session.getAttribute("randStr");
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String code=request.getParameter("code");
		String[] rem=request.getParameterValues("rem");
		
		
		String sql;
		PreparedStatement ps;
		ResultSet rs;
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out=response.getWriter();
		
		try
		{
			if(!randStr.equals(code)) out.print("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>验证码错误！</h4></div></div>");
			else if(name!=null && !name.equals("") && password!=null && !password.equals(""))
			{
				String shaPW=(new BigInteger(ToolBean.encryptSHA((password+name).getBytes()))).toString(16);
				sql="SELECT ID FROM FC_USER WHERE NAME=? AND PASSWORD=PASSWORD(?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, name);
				ps.setString(2, shaPW);
				rs = ps.executeQuery();
				if(rs.next())
				{
					String ip=ToolBean.getIpAddr(request);
					String id=rs.getString("ID");
					session.setAttribute("id", id);
					
					sql="UPDATE FC_USER SET LAST_IP=?,LAST_TIME=NOW() WHERE ID=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1,ip);
					ps.setString(2,id);
					ps.executeUpdate();
					if(rem!=null)
					{
						Cookie nameCookie = new Cookie("name",URLEncoder.encode(name,"UTF-8"));
						nameCookie.setPath("/");
						Cookie passwordCookie = new Cookie("password",URLEncoder.encode(password,"UTF-8"));
						passwordCookie.setPath("/");
						nameCookie.setMaxAge(3600*24*30);
						passwordCookie.setMaxAge(3600*24*30);
						response.addCookie(nameCookie);
						response.addCookie(passwordCookie);
					}
					
					out.print("true");
				}
				else
				{
					out.print("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>用户名或密码错误！</h4></div></div>");
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
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
