package fi.metropolia.neal.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProductDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Integer categoryId,
        LocalDate startDate,
        LocalDate endDate,
        List<Integer> supplierIds
) {}