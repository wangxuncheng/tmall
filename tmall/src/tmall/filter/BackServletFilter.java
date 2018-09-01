package tmall.filter;
 
import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.lang.StringUtils;
 
public class BackServletFilter implements Filter {
 
    public void destroy() {
         
    }
 
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
         
        String context = request.getServletContext().getContextPath();
        String uri = request.getRequestURI();
        String uris = StringUtils.remove(uri, context);
        if(uris.startsWith("/admin_")) {
        	String servletPath = StringUtils.substringBetween(uris, "_", "_") + "Servlet";
        	String method = StringUtils.substringAfterLast(uris, "_");
        	request.setAttribute("method", method);
        	
        	req.getRequestDispatcher("/"+servletPath).forward(request, response);
        	return ;
        }
        
        chain.doFilter(request, response);
    }
 
    public void init(FilterConfig arg0) throws ServletException {
     
    }
}