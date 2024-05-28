package vn.hust.api.dto.out.customter;

import lombok.Data;

@Data
public class ProfileDetailOut {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String avatar;
    private String fullName;
    private String phone;
    private String gender;
}
