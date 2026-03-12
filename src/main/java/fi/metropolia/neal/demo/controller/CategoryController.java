package fi.metropolia.neal.demo.controller;

import fi.metropolia.neal.demo.dto.CategoryDTO;
import fi.metropolia.neal.demo.dto.ProductDTO;
import fi.metropolia.neal.demo.service.ProductCategoryService;
import fi.metropolia.neal.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final ProductCategoryService categoryService;
    private final ProductService productService;

    public CategoryController(ProductCategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/products")
    public List<ProductDTO> getProductsByCategory(@PathVariable Integer id) {
        return productService.getProductsByCategory(id);
    }
}