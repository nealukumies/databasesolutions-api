package fi.metropolia.neal.demo.repository;
import fi.metropolia.neal.demo.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepo extends JpaRepository<Supplier, Integer> {
   
}