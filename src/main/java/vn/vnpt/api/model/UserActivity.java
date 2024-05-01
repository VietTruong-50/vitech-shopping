package vn.vnpt.api.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class UserActivity {
    private String customerId;
    private String eventType;
    private String eventDetail;
}
