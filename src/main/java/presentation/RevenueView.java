package presentation;

import business.IInvoiceService;
import business.impl.InvoiceServiceImpl;

import java.time.LocalDate;
import java.util.Scanner;

public class RevenueView {
    private final Scanner sc=new Scanner(System.in);
    IInvoiceService service=new InvoiceServiceImpl();
    public void menuRevenue(){
        while (true){
            System.out.print("============= THỐNG KÊ DOANH THU ================\n" +
                    "1. Doanh thu theo ngày\n" +
                    "2. Doanh thu theo tháng\n" +
                    "3. Doanh thu theo năm\n" +
                    "4. Quay lại menu chính\n" +
                    "=============================================================\n" +
                    "Chọn: ");
            int choice;
            try {
                choice=Integer.parseInt(sc.nextLine());
            }catch (Exception e){
                System.out.println("Invalid choice!");
                continue;
            }
            switch (choice){
                case 1 -> revenueByDay();
                case 2 -> revenueByMonth();
                case 3 -> revenueByYear();
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
    public void revenueByDay(){
        while (true) {
            try {
                System.out.print("Enter day (yyyy-MM-dd): ");
                String date = sc.nextLine();

                LocalDate.parse(date);

                double res = service.calRevenueByDay(date);

                if (res == 0) {
                    System.out.println("No revenue found for this day!");
                } else {
                    System.out.printf("Revenue on %s: %,.0f\n", date, res);
                }

                break;

            } catch (Exception e) {
                System.out.println("Invalid date format!");
            }
        }
    }
    public void revenueByMonth(){
        int month;
        int year;

        while (true) {
            try {
                System.out.print("Enter month: ");
                month = Integer.parseInt(sc.nextLine());

                System.out.print("Enter year: ");
                year = Integer.parseInt(sc.nextLine());

                if (month < 1 || month > 12) {
                    System.out.println("Month must be between 1 and 12!");
                    continue;
                }

                if (year <= 2000) {
                    System.out.println("Year must be > 0!");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid number!");
            }
        }
        double res = service.calRevenueByMonth(month, year);

        if (res == 0) {
            System.out.println("No revenue found for this period!");
        } else {
            System.out.printf("Revenue in %d/%d: %,.0f\n", month, year, res);
        }

    }
    public void revenueByYear(){
        try {
            int year=inputInt("Enter year: ");
            double res=service.calRevenueByYear(year);
            System.out.printf("Revenue in %d : %,.0f\n",year,res);
        }catch (Exception e){
            System.out.println(e.getMessage());
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
