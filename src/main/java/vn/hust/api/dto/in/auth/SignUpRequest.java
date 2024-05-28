package vn.hust.api.dto.in.auth;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String dob;
    private String password;
}
