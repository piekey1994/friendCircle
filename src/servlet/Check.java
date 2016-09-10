package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

public class Check extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	
	public Check() {
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
	
	//�û����Ƿ����Ҫ��
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
		//�����Ƿ����Ҫ��
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
		//�����Ƿ����Ҫ��
		public boolean emailOK(String email)
		{
			Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//����ƥ��  
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

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		 
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		String sql=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			if(name!=null)
			{
				if(name.length()<4) out.print("<div style=\"color: #FF0000\">����С��4</div>");
				else if(name.length()>20) out.print("<div style=\"color: #FF0000\">���ȴ���20</div>");
				else if(!nameOK(name)) out.print("<div style=\"color: #FF0000\">���ڲ��Ϸ��ַ�</div>");
				else
				{
					sql="SELECT NAME FROM FC_USER WHERE NAME=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, name);
					rs = ps.executeQuery();
					if(rs.next())
					{
						out.println("<div style=\"color: #FF0000\">�û����Ѵ���</div>");
					}
					else
					{
						out.println("<div style=\"color: #0db300\">��ȷ</div>");
					}
				}
			}
			else if(password!=null)
			{
				if(password.length()<6) out.print("<div style=\"color: #FF0000\">����С��6</div>");
				else if(password.length()>20) out.print("<div style=\"color: #FF0000\">���ȴ���20</div>");
				else if(!passwordOK(password)) out.print("<div style=\"color: #FF0000\">���ڲ��Ϸ��ַ�</div>");
				else
				{
					out.println("<div style=\"color: #0db300\">��ȷ</div>");
				}
			}
			else if(email!=null)
			{
				if(!emailOK(email)) out.print("<div style=\"color: #FF0000\">��ʽ����ȷ</div>");
				else
				{
					sql="SELECT NAME FROM FC_USER WHERE EMAIL=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, email);
					rs = ps.executeQuery();
					if(rs.next())
					{
						out.println("<div style=\"color: #FF0000\">�����Ѵ���</div>");
					}
					else out.println("<div style=\"color: #0db300\">��ȷ</div>");
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
