package dao;

import model.Product;

import java.util.List;

public interface IProductDAO {
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    List<Product> getAllProduct();
    List<Product> searchByBrand(String brand);
    List<Product> searchByPrice(double min, double max);
    List<Product> searchByNameAndStock(String name,int stock);
    List<Product> sortByPrice(boolean asc);
    Product getById(int id);

    boolean isExist(int id);

}
