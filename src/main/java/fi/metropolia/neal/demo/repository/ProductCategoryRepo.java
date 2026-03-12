package fi.metropolia.neal.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fi.metropolia.neal.demo.entity.ProductCategory;

public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Integer> { }