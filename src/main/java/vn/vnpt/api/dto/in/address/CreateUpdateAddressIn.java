package vn.vnpt.api.dto.in.address;

import lombok.Data;

@Data
public class CreateUpdateAddressIn {
    private String receiverName;

    private String phone;

    private String email;

    private String city;

    private String district;

    private String  subDistrict;

    private String specificAddress;

    private Boolean isLevant;
}
