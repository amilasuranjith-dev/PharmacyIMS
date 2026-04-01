package edu.icet.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SupplierDTO {
    private Integer id;
    private String name;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
}
