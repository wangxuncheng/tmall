package tmall.servlet;

import java.awt.image.BufferedImage;   
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.dao.ProductDAO;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class PropertyServlet extends BaseBackServlet {

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		
	    int cid = Integer.parseInt(request.getParameter("cid"));     
	    String name= request.getParameter("name");
	    Category c = categoryDAO.get(cid);
	    Property p = new Property();
	    p.setName(name);
	    p.setCategory(c);
	    propertyDAO.add(p);
		return "@admin_property_list?cid="+cid;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.get(id);
		propertyDAO.delete(id);
		return "@admin_property_list?cid="+p.getCategory().getId();
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.get(id);
		request.setAttribute("p", p);
		return "admin/editProperty.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		String name= request.getParameter("name");
	    int cid = Integer.parseInt(request.getParameter("cid"));    
	    int id = Integer.parseInt(request.getParameter("id"));
	    Category c = categoryDAO.get(cid);
	    Property p = propertyDAO.get(id);
	    p.setName(name);
	    p.setCategory(c);
	    propertyDAO.update(p);
		return "@admin_property_list?cid=" + cid;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
		List<Property> list = propertyDAO.list(cid, page.getStart(), page.getCount());
		page.setTotal(propertyDAO.getTotal(cid));
		page.setParam("&cid="+cid);
		request.setAttribute("c", c);
		request.setAttribute("ps", list);
		request.setAttribute("page", page);
		return "admin/listProperty.jsp";
	}

}
