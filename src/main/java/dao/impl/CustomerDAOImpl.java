package dao.impl;

import dao.ICustomerDAO;
import model.Customer;
import utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements ICustomerDAO {
    @Override
    public void insert(Customer customer) {
        String sql="Insert into Customer(name,phone,email,address) values(?,?,?,?)";
        try (Connection con= DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Customer customer) {
        String sql="Update Customer set name=?,phone=?,email=?,address=? Where id=?";
        try(Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setInt(5,customer.getId());
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        String sql="Delete from Customer Where id=?";
        try(Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id);
            ps.executeUpdate();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> getAllCustomer() {
        String sql="Select * From Customer";
        List<Customer> list=new ArrayList<>();
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql);
             ResultSet rs=ps.executeQuery()){
            while(rs.next()){
                list.add(new Customer(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Customer getCustomerById(int id) {
        String sql="Select * From Customer Where id=?";
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return new Customer(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }

}
