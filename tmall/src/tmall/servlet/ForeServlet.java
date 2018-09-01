package tmall.servlet;
 

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;

import com.mysql.jdbc.Util;

import tmall.bean.Category;
import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.bean.Review;
import tmall.bean.User;
import tmall.comparator.*;
import tmall.dao.CategoryDAO;
import tmall.dao.ProductDAO;
import tmall.util.Page;
 
public class ForeServlet extends BaseForeServlet {
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs= new CategoryDAO().list();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        request.setAttribute("cs", cs);
        return "home.jsp";
    }
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");
        if(userDAO.get(name) != null) {
        	String msg = "用户名已存在";
        	request.setAttribute("msg", msg);
        	return "register.jsp";
        }else {
        	User u = new User();
        	u.setName(name);
        	u.setPassword(password);
        	userDAO.add(u);
        	return "@registerSuccess.jsp";
        }
        
    }
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");
        User user = userDAO.get(name,password);
        if(user == null) {
        	String msg = "账号密码错误，请重新输入";
        	request.setAttribute("msg", msg);
        	return "login.jsp";
        }else {
        	request.getSession().setAttribute("user", user);
            return "@forehome";
        }
        
    }
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
    	request.getSession().removeAttribute("user");
        return "@forehome";     
    }
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
         
        List<ProductImage> productSingleImages = productImageDAO.list(p, productImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(p, productImageDAO.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);
         
        List<PropertyValue> pvs = propertyValueDAO.list(p.getId());      
     
        List<Review> reviews = reviewDAO.list(p.getId());
         
        productDAO.setSaleAndReviewNumber(p);
     
        request.setAttribute("reviews", reviews);
     
        request.setAttribute("p", p);
        request.setAttribute("pvs", pvs);
        return "product.jsp";      
    }  
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        if(null!=user)
            return "%success";
        return "%fail";
    }
    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");    
        User user = userDAO.get(name,password);
         
        if(null==user){
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success"; 
    }
    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
         
        Category c = new CategoryDAO().get(cid);
        new ProductDAO().fill(c);
        new ProductDAO().setSaleAndReviewNumber(c.getProducts());      
         
        String sort = request.getParameter("sort"); 
        if(null!=sort){
        switch(sort){
            case "review":
                Collections.sort(c.getProducts(),new ProductReviewComparator());
                break;
            case "date" :
                Collections.sort(c.getProducts(),new ProductDateComparator());
                break;
                 
            case "saleCount" :
                Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                break;
                 
            case "price":
                Collections.sort(c.getProducts(),new ProductPriceComparator());
                break;
                
            case "dprice":
                Collections.sort(c.getProducts(),new ProductdPriceComparator());
                break;
                
            case "all":
                Collections.sort(c.getProducts(),new ProductAllComparator());
                break;
            }
        }
        
        request.setAttribute("c", c);
        return "category.jsp";     
    }
    public String search(HttpServletRequest request, HttpServletResponse response, Page page) throws UnsupportedEncodingException {
    	String keyword = "123";
    	keyword = request.getParameter("outsideSearchKeyword");
    	System.out.println(keyword);
//    	String content = HtmlUtils.htmlEscape(text);	
//    	String keyword=java.net.URLDecoder.decode(text,"UTF-8"); 
//    	keyword = java.net.URLDecoder.decode(keyword,"UTF-8"); 
        List<Product> ps = null;
        page.setCount(8);
        if(keyword == null || keyword.length() == 0 || keyword.equals("null") || keyword.equals("NULL")) {
        	ps = productDAO.list(page.getStart(), page.getCount());
        	page.setTotal(productDAO.getTotal());
        }else {
        	page.setParam(keyword);
        	ps = productDAO.search(keyword,page.getStart(), page.getCount());
        	page.setTotal(productDAO.search(keyword).size());
        }
        
        new ProductDAO().setSaleAndReviewNumber(ps);       
        request.setAttribute("ps", ps);
        request.setAttribute("page", page);
        return "searchResult.jsp"; 
    }	
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
    	int num = Integer.parseInt(request.getParameter("num"));  
    	int oiid = 0;
    	Product p = productDAO.get(pid);
    	User user = (User) request.getSession().getAttribute("user");
    	List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
    	boolean found = false;
    	for(OrderItem oi : ois) {
    		if(oi.getProduct().getId() == pid) {
    			found = true;
    			int number = oi.getNumber();
    			number += num;
    			oi.setNumber(number);
    			orderItemDAO.update(oi);
    			oiid = oi.getId();
    			break;
    		}
    	}
    	if(!found) {
    		OrderItem oi = new OrderItem();
    		oi.setNumber(num);
    		oi.setUser(user);
    		oi.setProduct(p);
    		orderItemDAO.add(oi);
    		oiid = oi.getId();
    	}
        return "@forebuy?oiid="+oiid; 
    }
    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String[] oiids = request.getParameterValues("oiid");
    	List<OrderItem> ois = new ArrayList<OrderItem>();
    	float total = 0;
    	for(String strid : oiids) {
    		int oiid = Integer.parseInt(strid);
    		OrderItem oi = orderItemDAO.get(oiid);
    		ois.add(oi);
    		total += oi.getNumber()*oi.getProduct().getPromotePrice();
    	}
    	request.getSession().setAttribute("ois", ois);
    	request.setAttribute("total", total);
        return "buy.jsp"; 
    }
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
    	int num = Integer.parseInt(request.getParameter("num"));  
    	Product p = productDAO.get(pid);
    	User user = (User) request.getSession().getAttribute("user");
    	List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
    	boolean found = false;
    	for(OrderItem oi : ois) {
    		if(oi.getProduct().getId() == pid) {
    			found = true;
    			int number = oi.getNumber();
    			number += num;
    			oi.setNumber(number);
    			orderItemDAO.update(oi);
    			
    			break;
    		}
    	}
    	if(!found) {
    		OrderItem oi = new OrderItem();
    		oi.setNumber(num);
    		oi.setUser(user);
    		oi.setProduct(p);
    		orderItemDAO.add(oi);
 
    	}
    	return "%success";
    }
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
    	User user = (User) request.getSession().getAttribute("user");
    	List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
    	request.getSession().setAttribute("ois", ois);
    	return "@cart.jsp";
    }
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
    	int num = Integer.parseInt(request.getParameter("num"));
    	Product p = productDAO.get(pid);
    	User user = (User) request.getSession().getAttribute("user");
    	if(user == null) {
    		return "%fail";
    	}
    	List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
    	for(OrderItem oi : ois) {
    		if(oi.getProduct().getId() == pid) {
    			oi.setNumber(num);
    			orderItemDAO.update(oi);
    			
    			break;
    		}
    	}
    	return "%success";
    }
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oiid = Integer.parseInt(request.getParameter("oiid"));
    	
    	User user = (User) request.getSession().getAttribute("user");
    	if(user == null) {
    		return "%fail";
    	}
    	orderItemDAO.delete(oiid);
    	return "%success";
    }
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        User user =(User) request.getSession().getAttribute("user");
         
        List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
        if(ois.isEmpty())
            return "@login.jsp";
     
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");
         
        Order order = new Order();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +RandomUtils.nextInt(10000);
     
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(orderDAO.waitPay);
     
        orderDAO.add(order);
        float total =0;
        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
         
        return "@forealipay?oid="+order.getId() +"&total="+total;
    }
    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page){
        return "alipay.jsp";
    }
    public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(orderDAO.waitDelivery);
        order.setPayDate(new Date());
        orderDAO.update(order);
        request.setAttribute("o", order);
        return "payed.jsp";    
    }  
    public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
    	User user =(User) request.getSession().getAttribute("user");
    	List<Order> os = orderDAO.list(user.getId(), "delete");
    	orderItemDAO.fill(os);
    	request.setAttribute("os", os);
    	return "bought.jsp";  
    }
    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oid = Integer.parseInt(request.getParameter("oid"));
    	Order o = orderDAO.get(oid);
    	orderItemDAO.fill(o);
    	request.setAttribute("o", o);
    	return "confirmPay.jsp?oid=" + oid;
    }
    public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oid = Integer.parseInt(request.getParameter("oid"));
    	Order o = orderDAO.get(oid);
    	o.setConfirmDate(new Date());
    	o.setStatus(orderDAO.waitReview);
    	orderDAO.update(o);
    	return "orderConfirmed.jsp?oid=" + oid;
    }
    public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oid = Integer.parseInt(request.getParameter("oid"));
    	Order o = orderDAO.get(oid);
    	o.setStatus(orderDAO.delete);
    	orderDAO.update(o);
    	return "%success";
    }
    public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int oid = Integer.parseInt(request.getParameter("oid"));
//    	int pid = Integer.parseInt(request.getParameter("pid"));
    	Order o = orderDAO.get(oid);
    	orderItemDAO.fill(o);
    	List<OrderItem> ois = orderItemDAO.listByOrder(oid);
    	List<Product> ps = new ArrayList<Product>();
//    	Product p0 = o.getOrderItems().get(0).getProduct();
    	List<Review> reviews = reviewDAO.list();
    	for(OrderItem oi : ois) {
    		Product p = oi.getProduct();
//    		if(p.getId() == pid) {
//    			p.setIsreviewed(true);
//    		}
//    		List<Review> rs = reviewDAO.list(p.getId());
    		ps.add(p);
    	}
    	productDAO.setSaleAndReviewNumber(ps);
    	request.setAttribute("ps", ps);
    	request.setAttribute("reviews", reviews);
    	request.setAttribute("o", o);
    	return "review.jsp";
    }
    public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) throws UnsupportedEncodingException {
    	User user =(User) request.getSession().getAttribute("user");
    	int oid = Integer.parseInt(request.getParameter("oid"));
    	int pid = 0;
    	String[] pids = request.getParameterValues("pid");
    	String[] contents = request.getParameterValues("content");
    	
//    	int pid = Integer.parseInt(request.getParameter("pid"));
//    	String content = request.getParameter("content");
//        content = HtmlUtils.htmlEscape(content);
    	Order o = orderDAO.get(oid);
        o.setStatus(orderDAO.finish);
        orderDAO.update(o);
        for(int i = 0; i<pids.length; i++) {
    		pid = Integer.parseInt(pids[i]);
    		Product p = productDAO.get(pid);
    		Review r = new Review();
    		if(contents[i] != null && contents[i].length() != 0) {
    			String content = HtmlUtils.htmlEscape(contents[i].toString());	
    			String selStr=java.net.URLDecoder.decode(content,"UTF-8"); 
    			System.out.println(selStr);
    			r.setContent(selStr);
        		r.setCreateDate(new Date());
        		r.setProduct(p);
        		r.setUser(user);
        		reviewDAO.add(r);
    		}
    		
    	}   
//        r.setContent(content);
//        r.setCreateDate(new Date());
//        r.setProduct(p);
//        r.setUser(user);
//        reviewDAO.add(r);
//        boolean b = true;
//        p.setIsreviewed(b);
    	return "@forereview?oid="+oid+"&showonly=true"; 
    }
}