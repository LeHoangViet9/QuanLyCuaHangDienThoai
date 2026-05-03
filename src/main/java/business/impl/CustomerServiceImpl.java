package business.impl;

import business.ICustomerService;
import dao.ICustomerDAO;
import dao.impl.CustomerDAOImpl;
import model.Customer;

import java.util.List;

public class CustomerServiceImpl implements ICustomerService {
    private ICustomerDAO dao=new CustomerDAOImpl();
    @Override
    public void add(Customer customer) {
        validateCustomer(customer);
        dao.insert(customer);
    }

    private boolean isValidEmail(String email) {
        return email!=null&&email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidPhone(String phone) {

        return phone!=null&&phone.matches("^0\\d{9}$");
    }

    @Override
    public void update(Customer customer) {
        Customer old=dao.getCustomerById(customer.getId());
        if (old == null) {
            throw new IllegalArgumentException("Customer not found!");
        }
        validateCustomer(customer);
        dao.update(customer);
    }

    @Override
    public void delete(int id) {
        Customer customer=dao.getCustomerById(id);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found!");
        }
        try {
            dao.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Customer is used in invoice, cannot delete!");
        }
    }

    @Override
    public List<Customer> getAllCustomer() {

        return dao.getAllCustomer();
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer c = dao.getCustomerById(id);
        if (c == null) {
            throw new IllegalArgumentException("Customer not found!");
        }
        return c;
    }
    private void validateCustomer(Customer c) {
        if (c.getName() == null || c.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required!");
        }
        if (!isValidPhone(c.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number!");
        }
        if (!isValidEmail(c.getEmail())) {
            throw new IllegalArgumentException("Invalid email!");
        }
        if (c.getAddress() == null || c.getAddress().isBlank()) {
            throw new IllegalArgumentException("Address is required!");
        }
    }
}
