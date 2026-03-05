package fi.metropolia.neal.demo.dto;

import java.util.List;

public record SupplierDTO(
        Integer id,
        String name,
        String contactName,
        String phone,
        String email,
        List<Integer> productIds
) {}