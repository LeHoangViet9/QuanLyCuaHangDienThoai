package presentation;

import business.ICustomerService;
import business.impl.CustomerServiceImpl;
import dao.impl.InvoiceDAOImpl;
import model.Customer;

import java.util.List;
import java.util.Scanner;

public class CustomerView {
    private Scanner sc=new Scanner(System.in);
    ICustomerService service=new CustomerServiceImpl();
    public void menuCustomer(){
        while (true){
            System.out.print("============== QUẢN LÝ KHÁCH HÀNG ==============\n" +
                    "1. Hiển thị danh sách khách hàng\n" +
                    "2. Thêm khách hàng mới\n" +
                    "3. Cập nhập thông tin khách hàng\n" +
                    "4. Xóa khách hàng theo ID\n" +
                    "5. Quay lại menu chính\n" +
                    "===================================================================\n" +
                    "Chọn: ");
            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                System.out.println("Invalid choice");
                continue;
            }
            switch (choice){
                case 1 -> show();
                case 2 -> add();
                case 3 -> update();
                case 4 -> delete();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    private void add(){
        while (true) {
            try {
                String name;
                do {
                    System.out.print("Name: ");
                    name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty!");
                    }
                } while (name.isEmpty());
                String phone;
                do {
                    System.out.print("Phone: ");
                    phone = sc.nextLine().trim();
                    if (!phone.matches("^0\\d{9}$")) {
                        System.out.println("Invalid phone number!");
                    }
                } while (!phone.matches("^0\\d{9}$"));
                String email;
                do {
                    System.out.print("Email: ");
                    email = sc.nextLine().trim();
                    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        System.out.println("Invalid email!");
                    }
                } while (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"));
                String address;
                do {
                    System.out.print("Address: ");
                    address = sc.nextLine().trim();
                    if (address.isEmpty()) {
                        System.out.println("Address cannot be empty!");
                    }
                } while (address.isEmpty());

                service.add(new Customer(0, name, phone, email, address));
                System.out.println("Add successfully!");
                break;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void show() {
        List<Customer> list= service.getAllCustomer();
        if(list.isEmpty()){
            System.out.println("List is empty!");
            return;
        }
        System.out.println("========== CUSTOMER LIST ============");
        for(Customer c:list){
            System.out.println(c.getId()+" | "+
                    c.getName()+" | "+
                    c.getPhone()+" | "+
                    c.getEmail()+" | "+
                    c.getAddress()+" | ");
        }
    }
    private void delete(){
       try {
           int id=inputInt("Enter ID to delete: ");
           Customer c=service.getCustomerById(id);
           if(c==null){
               System.out.println("Customer Id not found!");
               return;
           }
           System.out.println("Are you sure to delete ?(y/n): ");
           String confirm=sc.nextLine();
           if(confirm.equalsIgnoreCase("y")){
               service.delete(id);
               System.out.println("Deleted!");
           }else{
               System.out.println("Cancelled!");
           }
       }catch (Exception e){
           System.out.println(e.getMessage());
       }

    }
    private void update(){
    while (true){
        try {
            int id=inputInt("Enter ID to update: ");
            Customer old=service.getCustomerById(id);
            if(old==null){
                System.out.println("Customer not found!");
                return;
            }
            System.out.println("Old data: ");
            System.out.println(old.getId()+" | "+
                    old.getName()+" | "+
                    old.getPhone()+" | "+
                    old.getEmail()+" | "+
                    old.getAddress()+" | ");
            System.out.print("New name ("+old.getName()+"): ");
            String name=sc.nextLine();
            if(name.isEmpty()) name= old.getName();
            String phone;
            do {
                System.out.print("New phone (" + old.getPhone() + "): ");
                phone = sc.nextLine().trim();
                if (phone.isEmpty()) {
                    phone = old.getPhone();
                    break;
                }
                if (!phone.matches("^0\\d{9}$")) {
                    System.out.println("Invalid phone number!");
                }
            } while (!phone.matches("^0\\d{9}$"));
            String email;
            do {
                System.out.print("New email (" + old.getEmail() + "): ");
                email = sc.nextLine().trim();
                if (email.isEmpty()) {
                    email = old.getEmail();
                    break;
                }
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    System.out.println("Invalid email!");
                }
            } while (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"));
            System.out.print("New address("+old.getAddress()+"): ");
            String address=sc.nextLine();
            if(address.isEmpty()) address=old.getAddress();
            service.update(new Customer(id,name,phone,email,address));
            System.out.println("Updated!");
            break;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    }
    private int inputInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number!");
            }
        }
    }
}
