package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Invoice {
    private int id;
    private int customer_id;
    private LocalDateTime created_at;
    private double total_amount;

    public Invoice() {
    }

    public Invoice(int id, int customer_id, LocalDateTime created_at, double total_amount) {
        this.id = id;
        this.customer_id = customer_id;
        this.created_at = created_at;
        this.total_amount = total_amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
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

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", created_at=" + created_at +
                ", total_amount=" + total_amount +
                '}';
    }
}
