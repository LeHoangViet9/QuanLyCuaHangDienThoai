package dao.impl;

import dao.IProductDAO;
import model.Product;
import utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements IProductDAO {
    @Override
    public void insert(Product product) {
        String sql="Insert into Product(name,brand,price,stock) values(?,?,?,?)";
        try (Connection con= DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,product.getName());
            ps.setString(2,product.getBrand());
            ps.setDouble(3,product.getPrice());
            ps.setInt(4,product.getStock());
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Product product) {
        String sql="Update Product SET name=?,brand=?,price=?,stock=? Where id=?";
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,product.getName());
            ps.setString(2,product.getBrand());
            ps.setDouble(3,product.getPrice());
            ps.setInt(4,product.getStock());
            ps.setInt(5,product.getId());
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql="Delete from Product Where id=?";
        try(Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAllProduct() {
        String sql="Select * From Product";
        List<Product> list=new ArrayList<>();
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql);
             ResultSet rs=ps.executeQuery()){
            while (rs.next()){
                list.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Product> searchByBrand(String brand) {
        String sql="Select * From Product Where brand ilike ?";
        List<Product> list=new ArrayList<>();
        try(Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1,"%"+brand+"%");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Product> searchByPrice(double min, double max) {
        String sql="Select * From Product Where price between ? and ?";
        List<Product> list=new ArrayList<>();
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setDouble(1,min);
            ps.setDouble(2,max);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")));
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return  list;
    }

    @Override
    public List<Product> searchByNameAndStock(String name,int stock) {
        String sql="Select * From Product Where name ilike ? and stock >= ?";
        List<Product> list=new ArrayList<>();
        try (Connection con=DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,"%"+name+"%");
            ps.setInt(2,stock);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")));
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return  list;
    }

    @Override
    public List<Product> sortByPrice(boolean asc) {
        String sql="Select * From Product ORDER BY price "+(asc ?"ASC" :"DESC");
        List<Product> list=new ArrayList<>();
        try (Connection con=DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(new Product(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")));
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return  list;
    }

    @Override
    public Product getById(int id) {
        String sql = "SELECT * FROM product WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public boolean updateStockWithConnection(Connection con,int id,int quantity) throws SQLException {
        String sql="UPDATE product SET stock=stock-? WHERE id=? and stock>=? ";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean isExist(int id){
        String sql = "SELECT 1 FROM Product WHERE id = ?";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
