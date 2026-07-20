package com.alex.company.platforSalesBackend.dto.customer;

import com.alex.company.platforSalesBackend.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private String customerId;
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

    public static CustomerResponse fromEntity(CustomerEntity user) {
        return new  CustomerResponse(
                user.getCustomerId(),
                user.getContactName(),
                user.getCompanyName(),
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
}
