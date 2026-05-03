package presentation;

import business.IProductService;
import business.impl.ProductServiceImpl;
import model.Product;

import java.util.List;
import java.util.Scanner;

public class ProductView {
    private final Scanner sc=new Scanner(System.in);
    IProductService service=new ProductServiceImpl();
    public void menuProduct(){
        while (true){
            System.out.print("========== QUẢN lÝ SẢN PHẨM ==========\n" +
                    "1. Hiển thị danh sách sản phẩm\n" +
                    "2. Thêm sản phẩm mới\n" +
                    "3. Cập nhập thông tin sản phẩm\n" +
                    "4. Xóa sản phẩm theo ID\n" +
                    "5. Tìm kiểm theo Brand\n" +
                    "6. Tìm kiếm theo khoảng giá\n" +
                    "7. Tìm kiếm theo tồn kho\n" +
                    "8. Quay lại menu chính\n" +
                    "=====================================================\n" +
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
                case 5 -> searchBrand();
                case 6 -> searchPrice();
                case 7 -> searchByNameAndStock();
                case 8 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");

            }
        }
    }

    private void add() {
        while (true) {
            try {
                String name=inputString("Name: ");

                String brand=inputString("Brand: ");

                double price;
                while (true) {
                    price = inputDouble("Price: ");
                    if (price <= 0) {
                        System.out.println("Price must be > 0!");
                    } else break;
                }

                int stock;
                while (true) {
                    stock = inputInt("Stock: ");
                    if (stock < 0) {
                        System.out.println("Stock must be >= 0!");
                    } else break;
                }

                service.add(new Product(0, name, brand, price, stock));
                System.out.println("Added successfully!");
                break;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    void show() {
        List<Product> list = service.getAllProduct();

        if (list.isEmpty()) {
            System.out.println("List is empty!");
            return;
        }

        System.out.println("\n--- PRODUCT LIST ---");
        printList(list);
    }

    private void update() {
       while (true){
           try {
               int id = inputInt("Enter ID to update: ");
               Product old=service.getById(id);
               if(old==null){
                   System.out.println("Product Id not found!");
                   continue;
               }
               System.out.println("Old data:");
               System.out.printf("%d | %s | %s | %,.0f | %d\n",
                       old.getId(),
                       old.getName(),
                       old.getBrand(),
                       old.getPrice(),
                       old.getStock());
               System.out.print("New name (" + old.getName() + "): ");
               String name = sc.nextLine();
               if (name.isEmpty()) name = old.getName();

               System.out.print("New brand (" + old.getBrand() + "): ");
               String brand = sc.nextLine();
               if (brand.isEmpty()) brand = old.getBrand();

               double price;
               while (true) {
                   System.out.print("New price (" + old.getPrice() + "): ");
                   String priceStr = sc.nextLine();
                   if (priceStr.isEmpty()) {
                       price = old.getPrice();
                       break;
                   }
                   try {
                       price = Double.parseDouble(priceStr);
                       break;
                   } catch (Exception e) {
                       System.out.println("Invalid price!");
                   }
               }

               int stock;
               while (true) {
                   System.out.print("New stock (" + old.getStock() + "): ");
                   String stockStr = sc.nextLine();
                   if (stockStr.isEmpty()) {
                       stock = old.getStock();
                       break;
                   }
                   try {
                       stock = Integer.parseInt(stockStr);
                       break;
                   } catch (Exception e) {
                       System.out.println("Invalid stock!");
                   }
               }

               service.update(new Product(id, name, brand, price, stock));
               System.out.println("Updated!");
               break;
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }

    private void delete() {
       try {
           int id = inputInt("Enter ID to delete: ");
           System.out.print("Are you sure to delete ? (y/n): ");
           String confirm = sc.nextLine();

           if (confirm.equalsIgnoreCase("y")) {
               service.delete(id);
               System.out.println("Deleted!");
           } else {
               System.out.println("Cancelled!");
           }
       }catch (Exception e){
           System.out.println("Cannot delete product because it is used in invoice!");
       }
    }

    private void searchBrand() {
      try {
          String brand = inputString("Brand: ");

          List<Product> list = service.searchBrand(brand);
          if(list.isEmpty()){
              System.out.println("List is empty!");
              return;
          }
          printList(list);
      }catch (Exception e){
          System.out.println(e.getMessage());
      }
    }

    private void searchPrice() {
        while (true) {
            try {
                double min = inputDouble("Min price: ");
                double max = inputDouble("Max price: ");
                if (min < 0 || max < 0) {
                    System.out.println("Price must be >= 0! Please re-enter.");
                    continue;
                }

                if (min > max) {
                    System.out.println("Min must <= Max! Please re-enter.");
                    continue;
                }

                List<Product> list = service.searchByPrice(min, max);

                if (list.isEmpty()) {
                    System.out.println("List is empty!");
                } else {
                    printList(list);
                }

                break;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }


    private void searchByNameAndStock() {
        while (true) {
            try {
                String name=inputString("Enter Product Name: ");

                int min;
                while (true) {
                    min = inputInt("Min stock: ");
                    if (min < 0) {
                        System.out.println("Stock must be >= 0!");
                    } else break;
                }

                List<Product> list = service.searchByNameAndStock(name, min);

                if (list.isEmpty()) {
                    System.out.println("No product found!");
                } else {
                    printList(list);
                }

                break;

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
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

    private double inputDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number!");
            }
        }
    }
    private String inputString(String msg) {
        while (true) {
            System.out.print(msg);
            String s = sc.nextLine().trim();

            if (!s.isEmpty()) {
                return s;
            }

            System.out.println("Cannot be empty!");
        }
    }
    private void printList(List<Product> list) {
        for (Product p : list) {
            System.out.printf("%d | %s | %s | %,.0f | %d\n",
                    p.getId(),
                    p.getName(),
                    p.getBrand(),
                    p.getPrice(),
                    p.getStock());
        }
    }
}


