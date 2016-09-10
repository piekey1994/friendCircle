package servlet;

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

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;

import beans.Dao;

public class SetAvatar extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	Dao dao;
	Connection conn;
	ServletConfig config;
	public SetAvatar() {
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
		PrintWriter out = response.getWriter();
		String sql=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		HttpSession session=request.getSession();
		
		String id=(String)session.getAttribute("id");
		if(id==null || id.equals("")) response.sendRedirect("/S2013150064/login.jsp");
		
		String picPath="";
		
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
					picPath="/avatar/"+System.currentTimeMillis()+fileType;
					smartFile.saveAs(picPath,smartUpload.SAVE_VIRTUAL);
				}
				
			}
		} catch (SmartUploadException e) {
			e.printStackTrace();
		}
		
		if(!picPath.equals(""))
		{
			try
			{
				sql="UPDATE FC_USER SET AVATAR=? WHERE ID=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, "/S2013150064"+picPath);
				ps.setString(2, id);
				ps.executeUpdate();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			response.sendRedirect("/S2013150064/index/set.jsp");
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
