package fi.metropolia.neal.demo.dto;

public record CustomerAddressDTO(
    Integer id,
    String streetAddress,
    String city,
    String postalCode,
    String country
) {
}
