package com.alex.company.platforSalesBackend.dto.customer;

import com.alex.company.platforSalesBackend.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;

    public static CustomerRequest fromRequest(CustomerEntity user) {
        return new  CustomerRequest (
                user.getCompanyName(),
                user.getContactName(),
                user.getContactTitle(),
                user.getAddress(),
                user.getCity(),
                user.getRegion(),
                user.getPostalCode(),
                user.getCountry(),
                user.getPhone(),
                user.getFax()
        );
    }

    public static CustomerEntity fromEntity(CustomerRequest entity) {
        CustomerEntity dto = new CustomerEntity();
        dto.setCompanyName(entity.getCompanyName());
        dto.setContactName(entity.getContactName());
        dto.setContactTitle(entity.getContactTitle());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setRegion(entity.getRegion());
        dto.setPostalCode(entity.getPostalCode());
        dto.setCountry(entity.getCountry());
        dto.setPhone(entity.getPhone());
        dto.setFax(entity.getFax());
        return dto;
    }
}
