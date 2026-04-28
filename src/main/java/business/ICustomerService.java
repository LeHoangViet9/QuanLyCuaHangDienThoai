package business;

import model.Customer;

import java.util.List;

public interface ICustomerService {
    void add(Customer customer);
    void update(Customer customer);
    void delete(int id);
    List<Customer> getAllCustomer();
    Customer getCustomerById(int id);
}
