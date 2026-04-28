package business.impl;

import business.IProductService;
import dao.IProductDAO;
import dao.impl.ProductDAOImpl;
import model.Product;

import java.util.List;

public class ProductServiceImpl implements IProductService {
    private IProductDAO dao=new ProductDAOImpl();
    @Override
    public void add(Product p) {
       validateProduct(p);
       dao.insert(p);
    }



    @Override
    public void update(Product p) {
        Product old=dao.getById(p.getId());
        if (old==null) {
            throw new IllegalArgumentException("Product not found!");
        }
        validateProduct(p);
        dao.update(p);
    }

    @Override
    public void delete(int id) {
        Product p=dao.getById(id);
        if (p==null) {
            throw new IllegalArgumentException("Product not found!");
        }
        dao.delete(id);
    }

    @Override
    public List<Product> getAllProduct() {

       return dao.getAllProduct();
    }

    @Override
    public List<Product> searchBrand(String brand) {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand is required!");
        }
        return dao.searchByBrand(brand);
    }

    @Override
    public List<Product> searchByPrice(double min, double max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Price must be >= 0");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min price must be <= max price!");
        }
        return dao.searchByPrice(min,max);
    }

    @Override
    public List<Product> searchByNameAndStock(String name,int stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required!");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
        return dao.searchByNameAndStock(name,stock);
    }

    @Override
    public List<Product> sortByPrice(boolean asc) {
        return dao.sortByPrice(asc);
    }

    @Override
    public Product getById(int id) {
        Product p=dao.getById(id);
        if(p==null){
           throw new IllegalArgumentException("Product not found!");
        }
        return p;
    }
    private void validateProduct(Product p) {
        if (p.getName() == null || p.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required!");
        }
        if (p.getBrand() == null || p.getBrand().isBlank()) {
            throw new IllegalArgumentException("Brand is required!");
        }
        if (p.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be > 0");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("Stock must be >= 0");
        }
    }

}
