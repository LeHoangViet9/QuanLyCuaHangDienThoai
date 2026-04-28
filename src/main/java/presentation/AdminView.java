package presentation;

import business.IAdminService;
import business.impl.AdminServiceImpl;

import java.util.Scanner;

public class AdminView {
    private final Scanner sc=new Scanner(System.in);
    IAdminService service=new AdminServiceImpl();
    public void menuAdmin(){
        while (true){
            System.out.print("========== ĐĂNG NHẬP QUẢN TRỊ ==============\n" +
                    "Tài khoản :");
            String username=sc.nextLine();
            String password=inputPassword();
            if(service.login(username,password)){
                System.out.println("Đăng nhập thành công!");
                new MainView().menu();
                break;
            }else{
                System.out.println("Sai tài khoản hoặc mật khẩu");
            }
        }
    }
    public String inputPassword() {
        java.io.Console console = System.console();

        if (console != null) {
            char[] pwd = console.readPassword("Mật khẩu: ");
            return new String(pwd);
        } else {
            System.out.print("Mật khẩu: ");
            return sc.nextLine();
        }
    }
}
