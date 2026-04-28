import presentation.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        AdminView adminView=new AdminView();

        while (true){
            System.out.print("============== HỆ THỐNG QUẢN LÝ CỬA HÀNG ===============\n" +
                    "1. Đăng nhập Admin\n" +
                    "2. Thoát\n" +
                    "========================================================\n" +
                    "Nhập lựa chọn: ");
            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());

            }catch (Exception e){
                System.out.println("Nhập số hợp lệ!");
                continue;
            }
            switch (choice) {
                case 1 -> adminView.menuAdmin();
                case 2 -> {
                    System.out.println("Thoát chương trình!");
                    return;
                }
                default -> System.out.println("Chọn sai!");
            }
        }
    }
}
