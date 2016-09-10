package beans;

import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

public class ToolBean {
	//特殊字符转换
	public static String changeToHtml(String input)
	{
		if(input == null || input.length() == 0)
            return "";
        char c = ' ';
        StringBuffer sb = new StringBuffer(input.length());
        for(int i = 0; i < input.length(); i++)
        {
            c = input.charAt(i);
            if(c == ' ')
            {
                sb.append("&nbsp;");
                continue;
            }
            if(c == '<')
            {
                sb.append("&lt;");
                continue;
            }
            if(c == '>')
            {
                sb.append("&gt;");
                continue;
            }
            if(c == '\n'){
                sb.append("<br>");
                continue;
            }
            if(c == '&' ){
            	sb.append("&amp;");
            	continue;
            }         
            else
                sb.append(c);
        }
        return sb.toString();
	}
	//获取客户端真实IP
		public static String getIpAddr(HttpServletRequest request) { 
	       String ip = request.getHeader("x-forwarded-for"); 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getHeader("WL-Proxy-Client-IP"); 
	       } 
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	           ip = request.getRemoteAddr(); 
	       } 
	       return ip; 
	   }
		//把系统时间转换为输出时间
		public static String getInputTime(String oldTime)
		{
			int month=Integer.valueOf(oldTime.substring(5, 7));
			int day=Integer.valueOf(oldTime.substring(8, 10));
			String hour=oldTime.substring(11, 13);
			String mintue=oldTime.substring(14, 16);
			return month+"月"+day+"日 "+hour+":"+mintue;
		}
		//SHA加密
		public static byte[] encryptSHA(byte[] data) throws Exception {  
			  
	        MessageDigest sha = MessageDigest.getInstance("SHA");  
	        sha.update(data);  
	  
	        return sha.digest();  
	  
	    }  
}
