package fi.metropolia.neal.demo.repository;
import fi.metropolia.neal.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {

}
