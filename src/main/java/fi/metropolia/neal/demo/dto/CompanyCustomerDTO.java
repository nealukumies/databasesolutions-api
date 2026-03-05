package fi.metropolia.neal.demo.dto;

public record CompanyCustomerDTO(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String companyName,
        String vatNumber
) {}