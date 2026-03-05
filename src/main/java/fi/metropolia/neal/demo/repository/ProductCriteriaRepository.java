package fi.metropolia.neal.demo.repository;
import fi.metropolia.neal.demo.entity.Product;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> findProductsByPriceRange(double minPrice, double maxPrice);
}