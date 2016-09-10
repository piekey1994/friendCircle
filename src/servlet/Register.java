package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Dao;
import beans.ToolBean;

public class Register extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	
	public Register() {
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
	
	//ÓÃ»§ÃûÊÇ·ñ·ûºÏÒªÇó
		public boolean nameOK(String name)
		{
			if(name.length()<4 || name.length()>20) return false;
			if(name.indexOf("<")!=-1) return false;
			if(name.indexOf(">")!=-1) return false;
			if(name.indexOf(" ")!=-1) return false;
			if(name.indexOf("&")!=-1) return false;
			if(name.indexOf("\n")!=-1) return false;
			if(name.indexOf("@")!=-1) return false;
			return true;
		}
		//ÃÜÂëÊÇ·ñ·ûºÏÒªÇó
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
		//ÓÊÏäÊÇ·ñ·ûºÏÒªÇó
		public boolean emailOK(String email)
		{
			Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//¸´ÔÓÆ¥Åä  
		    Matcher m = p.matcher(email);  
		    return m.matches();  
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
		
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		String ip=ToolBean.getIpAddr(request);
		
		String sql=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		if(name!=null && !name.equals("") && password!=null && !password.equals("") && email!=null && !email.equals(""))
		{
			try
			{
				if(!nameOK(name)) out.println("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>×¢²áÊ§°Ü£¡</h4></div></div>");
				else if(!passwordOK(password)) out.println("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>×¢²áÊ§°Ü£¡</h4></div></div>");
				else if(!emailOK(email)) out.println("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>×¢²áÊ§°Ü£¡</h4></div></div>");
				else
				{
					sql="SELECT NAME FROM FC_USER WHERE NAME=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, name);
					rs = ps.executeQuery();
					if(rs.next())
					{
						out.println("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>×¢²áÊ§°Ü£¡</h4></div></div>");
					}
					else
					{
						sql="SELECT NAME FROM FC_USER WHERE EMAIL=?";
						ps = conn.prepareStatement(sql);
						ps.setString(1, email);
						rs = ps.executeQuery();
						if(rs.next())
						{
							out.println("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #FF0000\"><h4>×¢²áÊ§°Ü£¡</h4></div></div>");
						}
						else
						{
							password=(new BigInteger(ToolBean.encryptSHA((password+name).getBytes()))).toString(16);
							sql = "INSERT INTO FC_USER(NAME,PASSWORD,EMAIL,IP) VALUES(?,PASSWORD(?),?,?)";
							ps = conn.prepareStatement(sql);
							ps.setString(1, name);
							ps.setString(2, password);
							ps.setString(3, email);
							ps.setString(4, ip);
							ps.executeUpdate();
							
							sql="SELECT ID FROM FC_USER WHERE NAME=?";
							ps = conn.prepareStatement(sql);
							ps.setString(1, name);
							rs = ps.executeQuery();
							String id="";
							if(rs.next()) id=rs.getString("ID");
							
							sql="INSERT INTO FC_RELATIONSHIP(ID,WHO) VALUES(?,?)";
							ps = conn.prepareStatement(sql);
							ps.setString(1, id);
							ps.setString(2, id);
							ps.executeUpdate();
							
							out.println("<div class=\"col-sm-offset-4 col-sm-5\"><div style=\"color: #0db300\"><h4>×¢²á³É¹¦£¡</h4></div></div>");
						}
					}
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
