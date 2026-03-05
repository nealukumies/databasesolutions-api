package fi.metropolia.neal.demo.repository;

import fi.metropolia.neal.demo.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findProductsByPriceRange(double minPrice, double maxPrice) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        query.select(product)
             .where(cb.between(product.get("price"), minPrice, maxPrice));

        return em.createQuery(query).getResultList();
    }
}