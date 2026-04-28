package model;

import java.time.LocalDateTime;

public class InvoiceDTO {
    private int id;
    private String name;
    private LocalDateTime created_at;
    private double total_amount;

    public InvoiceDTO() {
    }

    public InvoiceDTO(int id, String name, LocalDateTime created_at, double total_amount) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.total_amount = total_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }
}
