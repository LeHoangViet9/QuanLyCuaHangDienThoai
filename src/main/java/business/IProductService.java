package business;

import model.Product;

import java.util.List;

public interface IProductService {
    void add(Product product);
    void update(Product product);
    void delete(int id);
    List<Product> getAllProduct();
    List<Product> searchBrand(String brand);
    List<Product> searchByPrice(double min,double max);
    List<Product> searchByNameAndStock(String name,int stock);
    List<Product> sortByPrice(boolean asc);
    Product getById(int id);
}
