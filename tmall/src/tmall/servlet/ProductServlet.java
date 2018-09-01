package tmall.servlet;
import java.awt.image.BufferedImage;   
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.dao.ProductDAO;
import tmall.util.DateUtil;
import tmall.util.ImageUtil;
import tmall.util.Page;
public class ProductServlet extends BaseBackServlet{

	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));  
		Category c = categoryDAO.get(cid);
		String name= request.getParameter("name");
        String subTitle= request.getParameter("subTitle");
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        Timestamp createDate = DateUtil.d2t(new Date());
        Product p = new Product();
 
        p.setCategory(c);
        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOrignalPrice(orignalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setCreateDate(createDate); 
        
        productDAO.add(p);
        return "@admin_product_list?cid="+cid;
	}

	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		productDAO.delete(id);
		return "@admin_product_list?cid="+p.getCategory().getId();
	}

	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		request.setAttribute("p", p);
		return "admin/editProduct.jsp";
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));  
		Category c = categoryDAO.get(cid);
		String name= request.getParameter("name");
        String subTitle= request.getParameter("subTitle");
        float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        Timestamp createDate = DateUtil.d2t(new Date());
        Product p = new Product();
 
        p.setCategory(c);
        p.setName(name);
        p.setSubTitle(subTitle);
        p.setOrignalPrice(orignalPrice);
        p.setPromotePrice(promotePrice);
        p.setStock(stock);
        p.setCreateDate(createDate); 
        
        productDAO.add(p);
        return "@admin_product_list?cid="+cid;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
		List<Product> list = productDAO.list(cid, page.getStart(), page.getCount());
		page.setTotal(productDAO.getTotal(cid));
		page.setParam("&cid="+cid);
		request.setAttribute("c", c);
		request.setAttribute("ps", list);
		request.setAttribute("page", page);
		return "admin/listProduct.jsp";
	}
	
	public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		List<PropertyValue> pvs = propertyValueDAO.list(id);
		propertyValueDAO.init(p);
		request.setAttribute("p", p);
		request.setAttribute("pvs", pvs);
		return "admin/editProductValue.jsp";
	}
	
	public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		String value = request.getParameter("value");
		int id = Integer.parseInt(request.getParameter("pvid"));
		PropertyValue pv = propertyValueDAO.get(id);
		pv.setValue(value);
		propertyValueDAO.update(pv);
		return "%success";
	}
}
