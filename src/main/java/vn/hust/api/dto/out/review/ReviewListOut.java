package vn.hust.api.dto.out.review;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class ReviewListOut {
    @Col("comment_id")
    private String commentId;
    @Col("content")
    private String content;
    @Col("username")
    private String username;
    @Col("created_date")
    private String createdDate;
}
