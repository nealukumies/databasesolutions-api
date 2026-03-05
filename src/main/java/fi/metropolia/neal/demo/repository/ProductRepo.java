package fi.metropolia.neal.demo.repository;
import fi.metropolia.neal.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepo extends JpaRepository<Product, Integer>, ProductCriteriaRepository {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = p.price * (1 + :percentage / 100)")
    void updatePriceByPercentage(@Param("percentage") double percentage);
}
