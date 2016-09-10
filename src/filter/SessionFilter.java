package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter
{
	private FilterConfig filterConfig;
	
	public void destroy() 
	{
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		if((String)session.getAttribute("id")==null)
		{
			res.sendRedirect("/S2013150064/login.jsp");
		}
		else
		{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException 
	{
		this.filterConfig = filterConfig;
	}

}
