package dao.impl;

import dao.IIvoiceDAO;
import model.Invoice;
import model.InvoiceDTO;
import model.InvoiceDetail;
import utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOImpl implements IIvoiceDAO {
    @Override
    public int insertInvoice(Invoice invoice) {
        String sql="Insert into Invoice(customer_id,created_at,total_amount) values(?,?,?) ";
        try (Connection con= DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, invoice.getCustomer_id());
            ps.setTimestamp(2, Timestamp.valueOf(invoice.getCreated_at()));
            ps.setDouble(3,invoice.getTotal_amount());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return -1;
    }
    public int insertInvoiceWithConnection(Connection con, Invoice invoice) throws Exception {
        String sql = "Insert into Invoice(customer_id,created_at,total_amount) values(?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, invoice.getCustomer_id());
            ps.setTimestamp(2, Timestamp.valueOf(invoice.getCreated_at()));
            ps.setDouble(3, invoice.getTotal_amount());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("Insert invoice failed, no rows affected");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new RuntimeException("Insert invoice failed, no ID returned");
        }
    }

    @Override
    public void insertDetail(InvoiceDetail detail) {
        String sql="Insert into Invoice_Details(invoice_id,product_id,quantity,unit_price) values(?,?,?,?)";
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, detail.getInvoice_id());
            ps.setInt(2,detail.getProduct_id());
            ps.setInt(3,detail.getQuantity());
            ps.setDouble(4,detail.getUnit_price());
            ps.executeUpdate();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public boolean insertDetailWithConnection(Connection con, InvoiceDetail detail) throws Exception {
        String sql = "Insert into Invoice_Details(invoice_id,product_id,quantity,unit_price) values(?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, detail.getInvoice_id());
        ps.setInt(2, detail.getProduct_id());
        ps.setInt(3, detail.getQuantity());
        ps.setDouble(4, detail.getUnit_price());

        int rows=ps.executeUpdate();
        return rows>0;
    }


    @Override
    public List<Invoice> getAllInvoice() {
        String sql="Select * From Invoice";
        List<Invoice> list=new ArrayList<>();
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql);
             ResultSet rs=ps.executeQuery()){
            while (rs.next()){
                list.add(new Invoice(rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getDouble("total_amount")));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<InvoiceDTO> searchByCustomer(String name) {
        String sql="Select c.id,c.name as customerName,i.created_at, i.total_amount From Invoice i Join Customer c On i.customer_id=c.id Where c.name ilike ? ";
        List<InvoiceDTO> list=new ArrayList<>();
        try(Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql);
        ) {
           ps.setString(1,"%"+name+"%");
           ResultSet rs=ps.executeQuery();
           while (rs.next()){
               list.add(new InvoiceDTO(rs.getInt("id"),
                       rs.getString("customerName"),
                       rs.getTimestamp("created_at").toLocalDateTime(),
                       rs.getDouble("total_amount")));
           }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Invoice> searchByDate(String date) {
        String sql="Select * From Invoice Where created_at >= ? And created_at < ?";
        List<Invoice> list=new ArrayList<>();
        try(Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)) {
            LocalDate localDate=LocalDate.parse(date);
            Timestamp start=Timestamp.valueOf(localDate.atStartOfDay());
            Timestamp end=Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());
            ps.setTimestamp(1,start);
            ps.setTimestamp(2,end);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getDouble("total_amount")
                ));
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public double calRevenueByDay(String date) {
        String sql="Select Sum(total_amount) From Invoice Where Date(created_at)=?";
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            LocalDate localDate = LocalDate.parse(date);
            ps.setDate(1,java.sql.Date.valueOf(localDate));
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getDouble(1);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public double calRevenueByMonth(int month,int year) {
        String sql="Select Sum(total_amount) From Invoice Where Extract(Month From created_at)=? And Extract(Year From created_at)=?";
        try (Connection con=DBUtil.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,month);
            ps.setInt(2,year);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getDouble(1);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public double calRevenueByYear(int year) {
        String sql="Select Sum(total_amount) From Invoice Where  Extract(Year From created_at)=?";
        try (Connection con=DBUtil.getConnection();
             PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,year);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getDouble(1);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return 0;
    }


    @Override
    public boolean isExist(int id) {
        String sql = "SELECT 1 FROM Invoice WHERE id = ?";
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
