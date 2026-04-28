package dao.impl;

import dao.IAdminDAO;
import utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAOImpl implements IAdminDAO {
    @Override
    public boolean login(String username, String password) {
        String sql="Select * From Admin Where username=? and password=?";
        try (Connection con= DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs=ps.executeQuery();
            return rs.next();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
