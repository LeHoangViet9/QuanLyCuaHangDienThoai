package dao;

import model.Customer;

import java.util.List;

public interface ICustomerDAO {
    void insert(Customer customer);
    void update(Customer customer);
    void delete(int id);
    List<Customer> getAllCustomer();
    Customer getCustomerById(int id);

}
