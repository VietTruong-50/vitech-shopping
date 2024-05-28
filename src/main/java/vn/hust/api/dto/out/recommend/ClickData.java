package vn.hust.api.dto.out.recommend;

import lombok.Data;
import vn.hust.api.repository.helper.Col;

@Data
public class ClickData {
    @Col("event_detail")
    private String productId;
}
