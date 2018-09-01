package tmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.Review;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

public class ReviewDAO {
	public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select count(*) from review";
  
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
    }
  
    public void add(Review bean) {
  
        String sql = "insert into review values(null,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
            ps.setString(1, bean.getContent());
            ps.setInt(2, bean.getUser().getId());
            ps.setInt(3, bean.getProduct().getId());
            ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
            ps.execute();
  
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
    public void update(Review bean) {
  
        String sql = "update review set content= ?, uid = ?, pid =?, createDate = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
        	 ps.setString(1, bean.getContent());
             ps.setInt(2, bean.getUser().getId());
             ps.setInt(3, bean.getProduct().getId());
             ps.setTimestamp(4, DateUtil.d2t(bean.getCreateDate()));
             ps.execute();
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
  
    }
  
    public void delete(int id) {
  
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "delete from review where id = " + id;
  
            s.execute(sql);
  
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
    }
  
	public int getCount(int id) {
		int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
  
            String sql = "select count(*) from review where pid =" + id;
  
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return total;
	}
	public List<Review> list(int pid){
		
		return list(pid, 0, Short.MAX_VALUE);
		
	}
	public List<Review> list(int pid,int start,int count){
		List<Review> beans = new ArrayList<Review>();
		String sql = "select * from review where pid = ? order by id desc limit ?,? ";
		try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
			ps.setInt(1, pid);
			ps.setInt(2, start);
			ps.setInt(3, count);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Review r = new Review();
				r.setId(rs.getInt(1));
				r.setContent(rs.getString(2));
				r.setUser(new UserDAO().get(rs.getInt(3)));
				r.setProduct(new ProductDAO().get(rs.getInt(4)));
				r.setCreateDate(DateUtil.t2d(rs.getTimestamp(5)));
				beans.add(r);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return beans;
		
	}
	public List<Review> list(){
		List<Review> beans = new ArrayList<Review>();
		String sql = "select * from review order by id desc";
		try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Review r = new Review();
				r.setId(rs.getInt(1));
				r.setContent(rs.getString(2));
				r.setUser(new UserDAO().get(rs.getInt(3)));
				r.setProduct(new ProductDAO().get(rs.getInt(4)));
				r.setCreateDate(DateUtil.t2d(rs.getTimestamp(5)));
				beans.add(r);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return beans;
		
	}
}
