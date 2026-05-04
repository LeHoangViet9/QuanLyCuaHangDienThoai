package presentation;

import java.util.Scanner;

public class MainView {
    private Scanner sc = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("=========== MENU CHÍNH ===========");
            System.out.println("1. Quản lý sản phẩm điện thoại");
            System.out.println("2. Quản lý khách hàng");
            System.out.println("3. Quản lý hóa đơn");
            System.out.println("4. Thống kê doanh thu");
            System.out.println("5. Đăng xuất");
            System.out.print("Chọn: ");

            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                System.out.println("Invalid choice!");
                continue;
            }

            switch (choice) {
                case 1 -> new ProductView().menuProduct();
                case 2 -> new CustomerView().menuCustomer();
                case 3 -> new InvoiceView().menuInvoice();
                case 4 -> new RevenueView().menuRevenue();
                case 5 -> {
                    System.out.println("Logout!");
                    return;
                }
                default -> System.out.println("Invalic choice!");
            }
        }
    }
}
