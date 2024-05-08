package vn.vnpt.api.dto.out.customter;

import lombok.Data;
import vn.vnpt.api.dto.enums.GenderEnum;

@Data
public class UpdateProfileIn {
    private String username;
    private String email;
    private String lastName;
    private String firstName;
    private String fullName;
    private String phone;
    private String dob;
    private GenderEnum gender;
}
