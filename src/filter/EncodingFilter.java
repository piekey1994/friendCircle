package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class EncodingFilter implements Filter
{

	private FilterConfig filterConfig =null;
	private String filterEncoding =null;

	public void destroy() 
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException 
	{
		if(filterEncoding==null) filterEncoding="UTF-8";
		request.setCharacterEncoding(filterEncoding);
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setHeader("Cache-Control", "no-cache");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", -1);
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException 
	{
		this.filterConfig = filterConfig;
		filterEncoding=this.filterConfig.getInitParameter("encoding");
	}

}
