package dao;

import model.Invoice;
import model.InvoiceDTO;
import model.InvoiceDetail;

import java.util.List;

public interface IIvoiceDAO {
    int insertInvoice(Invoice invoice);
    void insertDetail(InvoiceDetail detail);
    List<Invoice> getAllInvoice();
    List<InvoiceDTO> searchByCustomer(String name);
    List<Invoice> searchByDate(String date);
    double calRevenueByDay(String date);
    double calRevenueByMonth(int month,int year);
    double calRevenueByYear(int year);
    boolean isExist(int id);
}
