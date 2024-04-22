package vn.vnpt.api.dto.in.review;

import lombok.Data;

@Data
public class CreateReviewIn {
    private String productId;

    private String content;
}
