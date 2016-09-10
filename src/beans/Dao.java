package beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Dao{
	Connection conn;
	PreparedStatement ps;
	String ip,dataName,user,password;
	public Dao() throws Exception
	{
		ip="ecs12.tomcats.pw";
		dataName="piekey";
		user="piekey";
		password="112119110";
		Class.forName("com.mysql.jdbc.Driver");
		conn=DriverManager.getConnection("jdbc:mysql://"+ip+":3306/"+dataName,user,password);
	}
	public void close() throws Exception
	{
		if(conn!=null) conn.close();
	}
	public Connection getConn()
	{
		return conn;
	}
}
