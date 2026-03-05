package fi.metropolia.neal.demo.dto;

public record CustomerDTO(
    Integer id,
    String firstName,
    String lastName,
    String email,
    String phone
) {}
