package presentation;


import business.ICustomerService;
import business.IInvoiceService;
import business.IProductService;
import business.impl.CustomerServiceImpl;
import business.impl.InvoiceServiceImpl;
import business.impl.ProductServiceImpl;
import model.Invoice;
import model.InvoiceDTO;
import model.InvoiceDetail;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InvoiceView {
    private final Scanner sc=new Scanner(System.in);
    IInvoiceService service=new InvoiceServiceImpl();
    ICustomerService customerService=new CustomerServiceImpl();
    IProductService productService=new ProductServiceImpl();
    public void menuInvoice(){
        while (true){
            System.out.print("=============== QUẢN LÝ HÓA ĐƠN ================\n" +
                    "1. Hiển thị danh sách hóa đơn\n" +
                    "2. Thêm mới hóa đơn\n" +
                    "3. Tìm kiếm hóa đơn\n" +
                    "4. Quay lại menu chính\n" +
                    "================================================\n" +
                    "Chọn: ");
            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                System.out.println("Invalid choice!");
                continue;
            }
            switch (choice){
                case 1 -> show();
                case 2 -> add();
                case 3 -> menuSearch();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    public void show(){
        List<Invoice> list=service.getAllInvoice();
        if(list.isEmpty()){
            System.out.println("List is empty!");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("=========== INVOICE LIST ===============");
        for(Invoice i:list){
            System.out.printf("%d | %d | %s | %,.0f\n",
                    i.getId(),
                    i.getCustomer_id(),
                    formatter.format(i.getCreated_at()),
                    i.getTotal_amount());
        }
    }
    public void add(){
        System.out.println("-----Product List----------");
        new ProductView().show();
      while (true){
          try {
              int customer_id;
              while (true) {
                  try {
                      customer_id = inputInt("Customer Id: ");
                      customerService.getCustomerById(customer_id);
                      break;
                  } catch (Exception e) {
                      System.out.println("Customer not exist!");
                  }
              }
              List<InvoiceDetail> details =new ArrayList<>();

              double total=0;
              while(true){
                  System.out.println("======== Enter product details =============");
                  int product_id;
                  while (true) {
                      try {
                          product_id = inputInt("Product Id: ");
                          productService.getById(product_id);
                          break;
                      } catch (Exception e) {
                          System.out.println("Product not exist!");
                      }
                  }
                  var product = productService.getById(product_id);


                  int quantity;
                  while (true) {
                      quantity = inputInt("Quantity: ");

                      if (quantity <= 0) {
                          System.out.println("Quantity must be > 0!");
                          continue;
                      }


                      int currentQty = 0;
                      for (InvoiceDetail d : details) {
                          if (d.getProduct_id() == product_id) {
                              currentQty = d.getQuantity();
                              break;
                          }
                      }

                      if (quantity + currentQty > product.getStock()) {
                          System.out.println("Not enough stock! Available: "
                                  + (product.getStock() - currentQty));
                      } else {
                          break;
                      }
                  }

                  double price = product.getPrice();
                  System.out.printf("Price: %,.0f\n", price);
                  boolean existed = false;

                  for (InvoiceDetail d : details) {
                      if (d.getProduct_id() == product_id) {
                          d.setQuantity(d.getQuantity() + quantity);
                          existed = true;
                          break;
                      }
                  }

                  if (!existed) {
                      InvoiceDetail detail = new InvoiceDetail();
                      detail.setProduct_id(product_id);
                      detail.setQuantity(quantity);
                      detail.setUnit_price(price);
                      details.add(detail);
                  }
                  total=0;
                  for (InvoiceDetail d : details) {
                      total += d.getQuantity() * d.getUnit_price();
                  }
                  String choice;
                  while (true) {
                      System.out.print("Add more product? (Y/N): ");
                      choice = sc.nextLine().trim();

                      if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n")) {
                          break;
                      }
                      System.out.println("Please enter Y or N!");
                  }
                  if (choice.equalsIgnoreCase("n")) {
                      break;
                  }
              }

              if(details.isEmpty()){
                  System.out.println("Invoice must have at least 1 product!");
                  continue;
              }
              Invoice invoice=new Invoice();
              invoice.setCustomer_id(customer_id);
              invoice.setCreated_at(java.time.LocalDateTime.now());
              invoice.setTotal_amount(total);
              service.createdInvoice(invoice,details);
              System.out.println("Add successfully!");
              break;
          }catch (Exception e){
              System.out.println(e.getMessage());
          }
      }

    }
    public void menuSearch(){
        while (true){
            System.out.print("============= Tìm kiếm hóa đơn =============\n" +
                    "1. Tìm kiếm theo tên khách hàng\n" +
                    "2. Tìm theo ngày/tháng/năm\n" +
                    "3. Quay lại menu hóa đơn\n" +
                    "Chọn: ");
            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                System.out.println("Invalid choice!");
                continue;
            }
            switch (choice){
                case 1 -> searchByName();
                case 2 -> searchByDate();
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    public void searchByName(){
       while (true){
           try {
               System.out.print("Enter Customer Name : ");
               String name=sc.nextLine();
               List<InvoiceDTO> list=service.searchByCustomer(name);
               if(list.isEmpty()){
                   System.out.println("List is empty!");
                   return;
               }
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
               for(InvoiceDTO i:list){
                   System.out.printf("%d | %s | %s | %,.0f\n",
                           i.getId(),
                           i.getName(),
                           i.getCreated_at().format(formatter),
                           i.getTotal_amount());
               }
               break;
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }
    public void searchByDate(){
        while (true) {
            try {
                System.out.print("Enter date (yyyy-MM-dd): ");
                String date = sc.nextLine();

                java.time.LocalDate.parse(date);

                List<Invoice> list = service.searchByDate(date);

                if (list.isEmpty()) {
                    System.out.println("List is empty!");
                    return;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (Invoice i : list) {
                    System.out.printf("%d | %d | %s | %,.0f\n",
                            i.getId(),
                            i.getCustomer_id(),
                            i.getCreated_at().format(formatter),
                            i.getTotal_amount());
                }
                break;

            } catch (Exception e) {
                System.out.println("Invalid date format!");
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
