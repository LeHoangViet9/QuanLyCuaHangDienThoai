package business.impl;

import business.IInvoiceService;
import dao.impl.InvoiceDAOImpl;
import dao.impl.ProductDAOImpl;
import model.Invoice;
import model.InvoiceDTO;
import model.InvoiceDetail;
import utils.DBUtil;

import java.sql.Connection;
import java.util.List;

public class InvoiceServiceImpl implements IInvoiceService {
    private InvoiceDAOImpl dao=new InvoiceDAOImpl();
    private ProductDAOImpl pdao=new ProductDAOImpl();
    @Override
    public void createdInvoice(Invoice invoice, List<InvoiceDetail> details) {
        if (details == null || details.isEmpty()) {
            throw new IllegalArgumentException("Invoice must have at least one detail");
        }

        Connection con = null;

        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            int invoiceId = dao.insertInvoiceWithConnection(con, invoice);

            if (invoiceId <= 0) {
                throw new RuntimeException("Insert invoice failed");
            }

            for (InvoiceDetail d : details) {

                if (d.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Quantity must be > 0");
                }

                if (d.getUnit_price() <= 0) {
                    throw new IllegalArgumentException("Price must be > 0");
                }

                if (!pdao.isExist(d.getProduct_id())) {
                    throw new RuntimeException("Product not found: " + d.getProduct_id());
                }
                boolean stockUpdated= pdao.updateStockWithConnection(con, d.getProduct_id(), d.getQuantity());
                if (!stockUpdated) {
                    throw new RuntimeException("Not enough stock for product id: " + d.getProduct_id());
                }

                d.setInvoice_id(invoiceId);

                boolean success = dao.insertDetailWithConnection(con, d);

                if (!success) {
                    throw new RuntimeException("Insert invoice detail failed");
                }
            }

            con.commit();

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }

            throw new RuntimeException("Create invoice failed", e);

        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Invoice> getAllInvoice() {
        return dao.getAllInvoice();
    }

    @Override
    public List<InvoiceDTO> searchByCustomer(String name) {
       if(name==null||name.isBlank()){
           throw  new IllegalArgumentException("Name cannot be empty");
       }
       return dao.searchByCustomer(name);
    }

    @Override
    public List<Invoice> searchByDate(String date) {
        if(date==null||date.isBlank()){
            throw  new IllegalArgumentException("Date cannot be empty");
        }
        return dao.searchByDate(date);
    }

    @Override
    public double calRevenueByDay(String date) {
        if(date==null||date.isBlank()){
            throw new IllegalArgumentException("Date cannot be empty");
        }
        return dao.calRevenueByDay(date);
    }

    @Override
    public double calRevenueByMonth(int month, int year) {
        if(month<1||month>12){
           throw  new IllegalArgumentException("Month must be between 1 and 12");
        }
        return dao.calRevenueByMonth(month,year);
    }

    @Override
    public double calRevenueByYear(int year) {
        if(year<=0){
           throw  new IllegalArgumentException("Year must be greater than 0");
        }
        return dao.calRevenueByYear(year);
    }
}
