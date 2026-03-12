package fi.metropolia.neal.demo.service;

import org.springframework.stereotype.Service;
import fi.metropolia.neal.demo.dto.CategoryDTO;
import fi.metropolia.neal.demo.entity.ProductCategory;
import fi.metropolia.neal.demo.repository.ProductCategoryRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepo categoryRepo;

    public ProductCategoryService(ProductCategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Integer id) {
        return categoryRepo.findById(id)
                .map(this::toDTO);
    }

    private CategoryDTO toDTO(ProductCategory category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }
}