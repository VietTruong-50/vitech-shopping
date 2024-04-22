package vn.vnpt.api.dto.in.auth;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
