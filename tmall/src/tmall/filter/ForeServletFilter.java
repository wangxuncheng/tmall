package tmall.filter;
 
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tmall.bean.*;
import tmall.dao.*;
import org.apache.commons.lang.StringUtils;
 
public class ForeServletFilter implements Filter {
 
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
        User user =(User) request.getSession().getAttribute("user");
        int cartTotalItemNumber= 0;
        if(null!=user){
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for (OrderItem oi : ois) {
                cartTotalItemNumber+=oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
         
        List<Category> cs=(List<Category>) request.getAttribute("cs");
        if(null==cs){
            cs=new CategoryDAO().list();
            request.setAttribute("cs", cs);        
        }
        if(uris.startsWith("/fore") && !uris.startsWith("/foreServlet")) {
        	
        	String method = StringUtils.substringAfterLast(uris, "fore");
        	request.setAttribute("method", method);
        	
        	req.getRequestDispatcher("/foreServlet").forward(request, response);
        	return ;
        }
        
        chain.doFilter(request, response);
    }
 
    public void init(FilterConfig arg0) throws ServletException {
     
    }
}