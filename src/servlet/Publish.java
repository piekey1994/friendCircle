package servlet;

import com.jspsmart.upload.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Dao;
import beans.ToolBean;

public class Publish extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	ServletConfig config;
	
	public Publish() {
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
		PrintWriter out=response.getWriter();
		
		String sql;
		PreparedStatement ps;
		ResultSet rs;
		HttpSession session=request.getSession();
		String id=(String)session.getAttribute("id");
		if(id==null || id.equals("")) response.sendRedirect("/S2013150064/login.jsp");
		String ip=ToolBean.getIpAddr(request);
		
		
		String picPath="";
		String msg="";
		String num="";
		
		SmartUpload smartUpload = new SmartUpload();
		//初始化	
		smartUpload.initialize(config, request, response); 
		try {
			//上传文件
			smartUpload.upload(); 
			//得到上传的文件对象
			File smartFile = smartUpload.getFiles().getFile(0);
			//保存文件
			if(!smartFile.isMissing() && smartFile.getSize()<5*1024*1024)
			{
				String fileType=smartFile.getFileName().substring(smartFile.getFileName().lastIndexOf("."));
				if(fileType.equals(".jpg") || fileType.equals(".jpeg") || fileType.equals(".png") || fileType.equals(".bmp") || fileType.equals(".gif"))
				{
					picPath="/pic/"+System.currentTimeMillis()+fileType;
					smartFile.saveAs(picPath,smartUpload.SAVE_VIRTUAL);
				}
				
			}
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
		
		String text= smartUpload.getRequest().getParameter("text");
		String authority=smartUpload.getRequest().getParameter("authority");
		String remind=smartUpload.getRequest().getParameter("remind");
		
		if(text!=null && !text.equals("") && authority!=null && !authority.equals("") && remind!=null && !remind.equals(""))
		{
			text=ToolBean.changeToHtml(text);
			if(text.length()>65535) msg="<div class=\"col-sm-offset-3 col-sm-6\"><div style=\"color: #FF0000\"><h4>留言内容超过长度限制(65535),发布失败</h4></div></div>";
			else
			{
				try
				{
					sql = "INSERT INTO FC_TEXT(ID,TEXT,PICTURE,IP) VALUES(?,?,?,?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, id);
					ps.setString(2, text);
					ps.setString(3, "/S2013150064"+picPath);
					ps.setString(4, ip);
					ps.executeUpdate();
					String mes_num="";
					sql="SELECT LAST_INSERT_ID()";
					ps = conn.prepareStatement(sql);
					rs = ps.executeQuery();
					if(rs.next()) mes_num=rs.getString("LAST_INSERT_ID()");
					if(authority.equals("all"))
					{
						sql = "INSERT INTO FC_VISIBLE(MES_NUM,ID,WHO) VALUES(?,?,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, mes_num);
						ps.setString(2, id);
						ps.setString(3, "-1");
						ps.executeUpdate();
					}
					else
					{
						sql = "INSERT INTO FC_VISIBLE(MES_NUM,ID,WHO) VALUES(?,?,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, mes_num);
						ps.setString(2, id);
						ps.setString(3, id);
						ps.executeUpdate();
						String[] seeID=smartUpload.getRequest().getParameterValues("seeID");
						if(seeID!=null)
						{
							for(String x:seeID)
							{
								sql = "INSERT INTO FC_VISIBLE(MES_NUM,ID,WHO) VALUES(?,?,?)";
								ps = conn.prepareStatement(sql);
								ps.setString(1, mes_num);
								ps.setString(2, id);
								ps.setString(3, x);
								ps.executeUpdate();
							}
						}
						
						
					}
					if(!remind.equals("no"))
					{
						String[] remindID=smartUpload.getRequest().getParameterValues("remindID");
						if(remindID!=null)
						for(String x:remindID)
						{
							sql = "INSERT INTO FC_REMIND(MES_NUM,ID,WHO) VALUES(?,?,?)";
							ps = conn.prepareStatement(sql);
							ps.setString(1, mes_num);
							ps.setString(2, id);
							ps.setString(3, x);
							ps.executeUpdate();
							sql="UPDATE FC_USER SET NATIVE_NUM=NATIVE_NUM+1 WHERE ID=?";
							ps = conn.prepareStatement(sql);
							ps.setString(1, x);
							ps.executeUpdate();
						}
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
				msg+="<div class=\"col-sm-offset-3 col-sm-6\"><div style=\"color: #0db300\"><h4>发布成功！</h4></div></div>";
			}
			
			session.setAttribute("msg", msg);
			response.sendRedirect("/S2013150064/index/index.jsp");
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
		config= this.getServletConfig();
		try {
			dao=new Dao();
			conn=dao.getConn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
