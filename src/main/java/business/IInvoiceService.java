package business;

import model.Invoice;
import model.InvoiceDTO;
import model.InvoiceDetail;

import java.util.List;

public interface IInvoiceService {
    void createdInvoice(Invoice invoice, List<InvoiceDetail>details);
    List<Invoice> getAllInvoice();
    List<InvoiceDTO> searchByCustomer(String name);
    List<Invoice> searchByDate(String date);
    double calRevenueByDay(String date);
    double calRevenueByMonth(int month, int year);
    double calRevenueByYear(int year);
}
