package websocket;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import beans.Dao;

@ServerEndpoint("/nativeSocket")  
public class nativeSocket {

	Dao dao;
	Connection conn;
	
	@OnMessage  
    public void onMessage(String message, Session session) throws IOException,  
            InterruptedException {  
		String sql;
		PreparedStatement ps;
		ResultSet rs;
		String id=message;
		String sentMessages="";
		try
		{
			 while (true) {    
	        	sql="SELECT NATIVE_NUM,APPLY_NUM FROM FC_USER WHERE ID=?";
	        	ps = conn.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();
				if(rs.next())
				{
					sentMessages=rs.getString("NATIVE_NUM")+","+rs.getString("APPLY_NUM");
				}
	        	
	            session.getBasicRemote().sendText(sentMessages); 
	            Thread.sleep(1000);
	        }   
		}catch(Exception e)
		{
			e.printStackTrace();
		}
       
    }  
  
    @OnOpen  
    public void onOpen() {  
    	try {
			dao=new Dao();
			conn=dao.getConn();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
  
    @OnClose  
    public void onClose() {  
    	try {
			dao.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
	
}
